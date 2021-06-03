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
import com.mojang.brigadier.arguments.*;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.oroarmor.json.brigadier.parsers.BrigadierToJsonParsers;

/**
 * The different argument parsers for the different argument types that brigadier supports.
 * More parsers can be added with {@link BrigadierArgumentParsers#register(Class, CommandNodeParser)}
 */
public final class BrigadierArgumentParsers {
    private static final Map<Class<? extends ArgumentType<?>>, CommandNodeParser> PARSERS = new HashMap<>();

    static {
        register(IntegerArgumentType.class, BrigadierToJsonParsers::parseInteger);
        register(BoolArgumentType.class, BrigadierToJsonParsers::parseBoolean);
        register(DoubleArgumentType.class, BrigadierToJsonParsers::parseDouble);
        register(FloatArgumentType.class, BrigadierToJsonParsers::parseFloat);
        register(StringArgumentType.class, BrigadierToJsonParsers::parseString);
        register(LongArgumentType.class, BrigadierToJsonParsers::parseLong);
    }

    /**
     * Gets the parser from the type
     *
     * @param type The type for the parser
     * @return The parser
     */
    public static CommandNodeParser get(Class<? extends ArgumentType<?>> type) {
        return PARSERS.get(type);
    }

    /**
     * Register a new parser type
     *
     * @param clazz   The class of the argument type
     * @param parser The parser for the class
     */
    public static void register(Class<? extends ArgumentType<?>> clazz, CommandNodeParser parser) {
        if (PARSERS.containsKey(clazz)) {
            throw new IllegalArgumentException(clazz + " already exists");
        }
        PARSERS.put(clazz, parser);
    }

    /**
     * A Functional Interface that parses argument types
     */
    @FunctionalInterface
    public interface CommandNodeParser {
        /**
         * Parses an argument type into a given json argument
         *
         * @param argument The JsonObject to store the properties of the argument into
         * @param type     The argument type to parse
         * @param <T>      The type of the argument type
         * @return An {@link ArgumentBuilder} representing this command node only
         */
        <T> void parse(JsonObject argument, ArgumentType<T> type);
    }
}
