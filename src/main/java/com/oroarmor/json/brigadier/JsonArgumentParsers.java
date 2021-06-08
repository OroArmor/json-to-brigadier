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

import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonObject;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.oroarmor.json.brigadier.parsers.JsonToBrigadierParsers;

/**
 * The different argument parsers for the different argument types that brigadier supports.
 * More parsers can be added with {@link JsonArgumentParsers#register(String, ArgumentParser)}
 */
public final class JsonArgumentParsers {
    private static final Map<String, ArgumentParser> PARSERS = new HashMap<>();

    static {
        register("brigadier:literal", JsonToBrigadierParsers::parseLiteral);
        register("brigadier:integer", JsonToBrigadierParsers::parseInteger);
        register("brigadier:boolean", JsonToBrigadierParsers::parseBoolean);
        register("brigadier:double", JsonToBrigadierParsers::parseDouble);
        register("brigadier:float", JsonToBrigadierParsers::parseFloat);
        register("brigadier:string", JsonToBrigadierParsers::parseString);
        register("brigadier:long", JsonToBrigadierParsers::parseLong);
    }

    /**
     * Register a new parser type
     *
     * @param type   The string for the parser type. Should follow {@code application:argument}.
     *               i.e. {@code brigadier:integer} for {@link com.mojang.brigadier.arguments.IntegerArgumentType}
     * @param parser The parser for the type
     */
    public static void register(String type, ArgumentParser parser) {
        if (PARSERS.containsKey(type)) {
            throw new IllegalArgumentException(type + " already exists");
        }
        PARSERS.put(type, parser);
    }

    /**
     * Gets the parser from the type
     *
     * @param type The type for the parser
     * @return The parser
     */
    public static ArgumentParser get(String type) {
        if (!PARSERS.containsKey(type)) {
            throw new IllegalArgumentException("Type " + type + " not found");
        }
        return PARSERS.get(type);
    }

    /**
     * A Functional Interface that parses arguments
     */
    @FunctionalInterface
    public interface ArgumentParser {
        /**
         * Parses a JsonObject into a command. Do not parse for children in this method
         *
         * @param commandObject The JsonObject for the command
         * @param <Type>        The type of the command context
         * @param <Self>        The {@link ArgumentBuilder} self type
         * @return An {@link ArgumentBuilder} representing this command node only
         */
        <Type, Self extends ArgumentBuilder<Type, Self>> ArgumentBuilder<Type, Self> parse(JsonObject commandObject);
    }
}
