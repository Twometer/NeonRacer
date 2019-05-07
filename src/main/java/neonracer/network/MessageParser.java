package neonracer.network;

import com.google.protobuf.InvalidProtocolBufferException;

interface MessageParser {
    void invoke(byte[] buffer, MessageHandler handler) throws InvalidProtocolBufferException;
}
