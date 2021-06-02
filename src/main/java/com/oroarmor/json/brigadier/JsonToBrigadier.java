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

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

public final class JsonToBrigadier {
    public static <T, S extends ArgumentBuilder<T, S>> ArgumentBuilder<T, S> parse(Path path) {
        String file;
        try {
            file = String.join("\n", Files.readAllLines(path));
        } catch (IOException e) {
            System.err.println("Invalid path to JSON file");
            throw new RuntimeException(e);
        }
        JsonObject commandObject = JsonParser.parseString(file).getAsJsonObject();

        LiteralArgumentBuilder<T> argumentBuilder = LiteralArgumentBuilder.literal(commandObject.get("name").getAsString());

        addChildren(argumentBuilder, commandObject);

        return parseCommand(commandObject);
    }

    private static <T, S extends ArgumentBuilder<T, S>> ArgumentBuilder<T, S> parseCommand(JsonObject commandObject) {
        if (!commandObject.has("argument")) {
            throw new IllegalArgumentException("Command is missing an argument type");
        }

        if (!commandObject.has("name")) {
            throw new IllegalArgumentException("Command is missing a name");
        }

        ArgumentBuilder<T, S> builder = ArgumentParsers.get(commandObject.get("argument").getAsJsonObject().get("type").getAsString()).parse(commandObject);
        addChildren(builder, commandObject);

        if (commandObject.has("executes")) {
            String[] description = commandObject.get("executes").getAsString().split("::");
            Class<?> executeClass;
            try {
                executeClass = JsonToBrigadier.class.getClassLoader().loadClass(description[0]);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

            Method method = null;
            try {
                method = executeClass.getDeclaredMethod(description[1], CommandContext.class);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            System.out.println(method);

            Method finalMethod = method;
            builder.executes(source -> {
                try {
                    return (Integer) finalMethod.invoke(source);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }

        return builder;
    }

    private static <T, S extends ArgumentBuilder<T, S>> void addChildren(ArgumentBuilder<T, S> argumentBuilder, JsonObject commandObject) {
        if (!commandObject.has("children")) {
            return;
        }

        for (JsonElement child : commandObject.get("children").getAsJsonArray()) {
            argumentBuilder.then(parseCommand(child.getAsJsonObject()));
        }
    }
}
