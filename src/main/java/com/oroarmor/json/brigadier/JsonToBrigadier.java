/*
 * MIT License
 *
 * Copyright (c) 2021 OroArmor (Eli Orona)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.oroarmor.json.brigadier;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Predicate;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import static com.oroarmor.json.brigadier.StringConstants.*;

/**
 * Parses JSON files into {@link ArgumentBuilder}s for your {@link com.mojang.brigadier.CommandDispatcher}
 */
public final class JsonToBrigadier {

    /**
     * Parses a file at the path
     *
     * @param path The path to the JSON file
     * @param <T>  The command context type
     * @param <S>  The {@link ArgumentBuilder} self type
     * @return An {@link ArgumentBuilder} for the JSON file
     */
    public static <T, S extends ArgumentBuilder<T, S>> ArgumentBuilder<T, S> parse(Path path) {
        String file;
        try {
            file = String.join("\n", Files.readAllLines(path));
        } catch (IOException e) {
            System.err.println("Invalid path to JSON file");
            throw new RuntimeException(e);
        }
        return parse(file);
    }

    /**
     * Parses a json string
     *
     * @param json The string for the json
     * @param <T>  The command context type
     * @param <S>  The {@link ArgumentBuilder} self type
     * @return An {@link ArgumentBuilder} for the JSON file
     */
    public static <T, S extends ArgumentBuilder<T, S>> ArgumentBuilder<T, S> parse(String json) {
        JsonObject commandObject = JsonParser.parseString(json).getAsJsonObject();
        return parseCommand(commandObject);
    }

    private static <T, S extends ArgumentBuilder<T, S>> ArgumentBuilder<T, S> parseCommand(JsonObject commandObject) {
        if (!commandObject.has(ARGUMENT)) {
            throw new IllegalArgumentException("Command is missing an argument type");
        }

        if (!commandObject.has(NAME)) {
            throw new IllegalArgumentException("Command is missing a name");
        }

        ArgumentBuilder<T, S> builder = JsonArgumentParsers.get(commandObject.get("argument").getAsJsonObject().get("type").getAsString()).parse(commandObject);
        addChildren(builder, commandObject);

        if (commandObject.has(EXECUTES)) {
            String[] description = commandObject.get(EXECUTES).getAsString().split("::");
            Class<?> executeClass;
            try {
                executeClass = Thread.currentThread().getContextClassLoader().loadClass(description[0]);
                final Method method = executeClass.getDeclaredMethod(description[1], CommandContext.class);
                builder.executes(new Command<T>() {
                    @Override
                    public int run(CommandContext<T> context) {
                        try {
                            return (Integer) method.invoke(null, context);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public String toString() {
                        return commandObject.get(EXECUTES).getAsString();
                    }
                });
            } catch (ClassNotFoundException | NoSuchMethodException e) {
                System.err.println(e.getMessage());
                builder.executes(source -> {
                    System.err.println("Unable to find method for " + commandObject.get(EXECUTES));
                    return 0;
                });
            }
        }

        if (commandObject.has(REQUIRES)) {
            String[] description = commandObject.get(REQUIRES).getAsString().split("::");
            Class<?> executeClass;
            try {
                executeClass = Thread.currentThread().getContextClassLoader().loadClass(description[0]);
                final Method method = executeClass.getDeclaredMethod(description[1], Object.class);
                builder.requires(new Predicate<T>() {
                    public boolean test(T context) {
                        try {
                            return (Boolean) method.invoke(null, context);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public String toString() {
                        return commandObject.get(REQUIRES).getAsString();
                    }
                });
            } catch (ClassNotFoundException | NoSuchMethodException e) {
                System.err.println(e.getMessage());
                builder.requires(source -> {
                    System.err.println("Unable to find method for " + commandObject.get(REQUIRES));
                    return false;
                });
            }
        }

        return builder;
    }

    private static <T, S extends ArgumentBuilder<T, S>> void addChildren(ArgumentBuilder<T, S> argumentBuilder, JsonObject commandObject) {
        if (!commandObject.has(CHILDREN)) {
            return;
        }

        for (JsonElement child : commandObject.get(CHILDREN).getAsJsonArray()) {
            argumentBuilder.then(parseCommand(child.getAsJsonObject()));
        }
    }
}
