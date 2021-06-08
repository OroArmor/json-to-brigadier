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

public final class BrigadierToJsonParsers {
    public static <T> void parseBoolean(JsonObject argument, ArgumentType<T> argumentType) {
        argument.addProperty("type", "brigadier:boolean");
    }

    public static <T> void parseDouble(JsonObject argument, ArgumentType<T> argumentType) {
        argument.addProperty("type", "brigadier:double");
        DoubleArgumentType doubleArgument = (DoubleArgumentType) argumentType;
        if (doubleArgument.getMaximum() != Double.MAX_VALUE) {
            argument.addProperty("max", doubleArgument.getMaximum());
            argument.addProperty("min", doubleArgument.getMinimum());
        } else if (doubleArgument.getMinimum() != -Double.MAX_VALUE) {
            argument.addProperty("min", doubleArgument.getMinimum());
        }
    }

    public static <T> void parseFloat(JsonObject argument, ArgumentType<T> argumentType) {
        argument.addProperty("type", "brigadier:float");
        FloatArgumentType floatArgument = (FloatArgumentType) argumentType;
        if (floatArgument.getMaximum() != Float.MAX_VALUE) {
            argument.addProperty("max", floatArgument.getMaximum());
            argument.addProperty("min", floatArgument.getMinimum());
        } else if (floatArgument.getMinimum() != -Float.MAX_VALUE) {
            argument.addProperty("min", floatArgument.getMinimum());
        }
    }

    public static <T> void parseInteger(JsonObject argument, ArgumentType<T> argumentType) {
        argument.addProperty("type", "brigadier:integer");
        IntegerArgumentType integerArgument = (IntegerArgumentType) argumentType;
        if (integerArgument.getMaximum() != Integer.MAX_VALUE) {
            argument.addProperty("max", integerArgument.getMaximum());
            argument.addProperty("min", integerArgument.getMinimum());
        } else if (integerArgument.getMinimum() != -Integer.MAX_VALUE) {
            argument.addProperty("min", integerArgument.getMinimum());
        }
    }


    @SuppressWarnings("unchecked")
    public static <T> void parseLong(JsonObject argument, ArgumentType<T> argumentType) {
        argument.addProperty("type", "brigadier:long");
        LongArgumentType longArgument = (LongArgumentType) argumentType;
        if (longArgument.getMaximum() != Long.MAX_VALUE) {
            argument.addProperty("max", longArgument.getMaximum());
            argument.addProperty("min", longArgument.getMinimum());
        } else if (longArgument.getMinimum() != -Long.MAX_VALUE) {
            argument.addProperty("min", longArgument.getMinimum());
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> void parseString(JsonObject argument, ArgumentType<T> argumentType) {
        argument.addProperty("type", "brigadier:string");
        StringArgumentType stringArgument = (StringArgumentType) argumentType;
        String type = switch (stringArgument.getType()) {
            case GREEDY_PHRASE -> "greedy";
            case SINGLE_WORD -> "word";
            case QUOTABLE_PHRASE -> "string";
        };

        argument.addProperty("string_type", type);
    }
}
