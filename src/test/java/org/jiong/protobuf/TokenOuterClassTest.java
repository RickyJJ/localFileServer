package org.jiong.protobuf;

import com.google.protobuf.*;
import com.google.protobuf.util.JsonFormat;
import com.google.protobuf.util.Structs;
import com.google.protobuf.util.Values;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class TokenOuterClassTest {
    @Test
    public void writeToProtobufTest() throws IOException {
        TokenInfo.Token token = TokenInfo.Token.newBuilder()
                .setType(TokenInfo.Token.TokenType.FOREVER).build();

        FileOutputStream outputStream = new FileOutputStream("test.token");
        token.writeTo(outputStream);
    }

    @Test
    public void readFromProtobufTest() throws IOException {
        FileInputStream inputStream = new FileInputStream("test.token");
        TokenInfo.Token token = TokenInfo.Token.parseFrom(inputStream);

        System.out.println(token.getValue() == null);
        System.out.println("".equals(token.getValue()));
        System.out.println(token.getType().name());

    }

    @Test
    public void protobufJsonTest() throws IOException {
        Struct struct = Structs.of("token1", Values.of("tokenValue"));

        FileOutputStream out = new FileOutputStream("struct.token");

        struct.writeTo(out);

        FileInputStream inputStream = new FileInputStream("struct.token");

        TokenInfo.Token token = TokenInfo.Token.parseFrom(inputStream);
    }

    @Test
    public void protobufJsonReadTest() throws IOException {
        FileInputStream inputStream = new FileInputStream("struct.token");
//        Value string = Values.of("String");

//        TokenInfo.Token token = TokenInfo.Token.parseFrom(inputStream);
        Struct struct = Struct.parseFrom(inputStream);
//        Descriptors.FieldDescriptor token2 = TokenInfo.Token.getDescriptor().findFieldByNumber(1);
        Value token1 = struct.getFieldsOrThrow("token1");
        System.out.println(token1.getStringValue());
        System.out.println(token1.toString());


//        struct.getFieldsMap().forEach((k, v) -> {
//            System.out.println(k + " : "+ v);
//        });
//        String value = token.getValue();
//        System.out.println(value);
//        TokenInfo.Token.Builder builder = TokenInfo.Token.newBuilder();
//        JsonFormat.parser().ignoringUnknownFields().merge("{\"value\": \"val1\", \"value1\": \"val1\"}", builder);

//        String value = builder.build().getValue();
//        System.out.println(value);

    }

    @Test
    public void structTest() throws IOException {
        FileOutputStream outputStream = new FileOutputStream("test.struct");
        Struct struct1 = Structs.of("newToken1", Values.of("newTokenVal2"));
        struct1.writeTo(outputStream);

    }
}
