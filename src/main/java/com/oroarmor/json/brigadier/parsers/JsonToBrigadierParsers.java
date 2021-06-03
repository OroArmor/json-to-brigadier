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

package com.oroarmor.json.brigadier.parsers;

import com.google.gson.JsonObject;
import com.mojang.brigadier.arguments.*;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;

public final class JsonToBrigadierParsers {
    @SuppressWarnings("unchecked")
    public static <T, S extends ArgumentBuilder<T, S>> ArgumentBuilder<T, S> parseBoolean(JsonObject object) {
        BoolArgumentType booleanArgument = BoolArgumentType.bool();
        return (ArgumentBuilder<T, S>) RequiredArgumentBuilder.argument(object.get("name").getAsString(), booleanArgument);
    }

    @SuppressWarnings("unchecked")
    public static <T, S extends ArgumentBuilder<T, S>> ArgumentBuilder<T, S> parseDouble(JsonObject object) {
        DoubleArgumentType doubleArgument;
        JsonObject argument = object.get("argument").getAsJsonObject();
        if (argument.has("min")) {
            double min = argument.get("min").getAsDouble();
            if (argument.has("max")) {
                doubleArgument = DoubleArgumentType.doubleArg(min, argument.get("max").getAsDouble());
            } else {
                doubleArgument = DoubleArgumentType.doubleArg(min);
            }
        } else {
            doubleArgument = DoubleArgumentType.doubleArg();
        }

        return (ArgumentBuilder<T, S>) RequiredArgumentBuilder.argument(object.get("name").getAsString(), doubleArgument);
    }

    @SuppressWarnings("unchecked")
    public static <T, S extends ArgumentBuilder<T, S>> ArgumentBuilder<T, S> parseFloat(JsonObject object) {
        FloatArgumentType floatArgument;
        JsonObject argument = object.get("argument").getAsJsonObject();
        if (argument.has("min")) {
            float min = argument.get("min").getAsFloat();
            if (argument.has("max")) {
                floatArgument = FloatArgumentType.floatArg(min, argument.get("max").getAsFloat());
            } else {
                floatArgument = FloatArgumentType.floatArg(min);
            }
        } else {
            floatArgument = FloatArgumentType.floatArg();
        }

        return (ArgumentBuilder<T, S>) RequiredArgumentBuilder.argument(object.get("name").getAsString(), floatArgument);
    }

    @SuppressWarnings("unchecked")
    public static <T, S extends ArgumentBuilder<T, S>> ArgumentBuilder<T, S> parseInteger(JsonObject object) {
        IntegerArgumentType integerArgument;
        JsonObject argument = object.get("argument").getAsJsonObject();
        if (argument.has("min")) {
            int min = argument.get("min").getAsInt();
            if (argument.has("max")) {
                integerArgument = IntegerArgumentType.integer(min, argument.get("max").getAsInt());
            } else {
                integerArgument = IntegerArgumentType.integer(min);
            }
        } else {
            integerArgument = IntegerArgumentType.integer();
        }

        return (ArgumentBuilder<T, S>) RequiredArgumentBuilder.argument(object.get("name").getAsString(), integerArgument);
    }

    @SuppressWarnings("unchecked")
    public static <T, S extends ArgumentBuilder<T, S>> ArgumentBuilder<T, S> parseLiteral(JsonObject object) {
        return (ArgumentBuilder<T, S>) LiteralArgumentBuilder.literal(object.get("name").getAsString());
    }

    @SuppressWarnings("unchecked")
    public static <T, S extends ArgumentBuilder<T, S>> ArgumentBuilder<T, S> parseLong(JsonObject object) {
        LongArgumentType longArgument;
        JsonObject argument = object.get("argument").getAsJsonObject();
        if (argument.has("min")) {
            int min = argument.get("min").getAsInt();
            if (argument.has("max")) {
                longArgument = LongArgumentType.longArg(min, argument.get("max").getAsInt());
            } else {
                longArgument = LongArgumentType.longArg(min);
            }
        } else {
            longArgument = LongArgumentType.longArg();
        }

        return (ArgumentBuilder<T, S>) RequiredArgumentBuilder.argument(object.get("name").getAsString(), longArgument);
    }

    @SuppressWarnings("unchecked")
    public static <T, S extends ArgumentBuilder<T, S>> ArgumentBuilder<T, S> parseString(JsonObject object) {
        StringArgumentType stringArgument;
        JsonObject argument = object.get("argument").getAsJsonObject();

        if (argument.has("string_type")) {
            stringArgument = switch (argument.get("string_type").getAsString()) {
                case "word" -> StringArgumentType.word();
                case "greedy" -> StringArgumentType.greedyString();
                default -> StringArgumentType.string();
            };
        } else {
            stringArgument = StringArgumentType.word();
        }

        return (ArgumentBuilder<T, S>) RequiredArgumentBuilder.argument(object.get("name").getAsString(), stringArgument);
    }
}
