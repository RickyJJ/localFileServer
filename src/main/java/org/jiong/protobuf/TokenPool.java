// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: tokenPage.proto

package org.jiong.protobuf;

public final class TokenPool {
  private TokenPool() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  public interface TokenPageOrBuilder extends
      // @@protoc_insertion_point(interface_extends:org.jiong.protobuf.TokenPage)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <code>repeated .org.jiong.protobuf.Token token = 1;</code>
     */
    java.util.List<org.jiong.protobuf.TokenInfo.Token> 
        getTokenList();
    /**
     * <code>repeated .org.jiong.protobuf.Token token = 1;</code>
     */
    org.jiong.protobuf.TokenInfo.Token getToken(int index);
    /**
     * <code>repeated .org.jiong.protobuf.Token token = 1;</code>
     */
    int getTokenCount();
    /**
     * <code>repeated .org.jiong.protobuf.Token token = 1;</code>
     */
    java.util.List<? extends org.jiong.protobuf.TokenInfo.TokenOrBuilder> 
        getTokenOrBuilderList();
    /**
     * <code>repeated .org.jiong.protobuf.Token token = 1;</code>
     */
    org.jiong.protobuf.TokenInfo.TokenOrBuilder getTokenOrBuilder(
        int index);
  }
  /**
   * Protobuf type {@code org.jiong.protobuf.TokenPage}
   */
  public  static final class TokenPage extends
      com.google.protobuf.GeneratedMessageV3 implements
      // @@protoc_insertion_point(message_implements:org.jiong.protobuf.TokenPage)
      TokenPageOrBuilder {
  private static final long serialVersionUID = 0L;
    // Use TokenPage.newBuilder() to construct.
    private TokenPage(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
      super(builder);
    }
    private TokenPage() {
      token_ = java.util.Collections.emptyList();
    }

    @java.lang.Override
    @SuppressWarnings({"unused"})
    protected java.lang.Object newInstance(
        UnusedPrivateParameter unused) {
      return new TokenPage();
    }

    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
    getUnknownFields() {
      return this.unknownFields;
    }
    private TokenPage(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      this();
      if (extensionRegistry == null) {
        throw new java.lang.NullPointerException();
      }
      int mutable_bitField0_ = 0;
      com.google.protobuf.UnknownFieldSet.Builder unknownFields =
          com.google.protobuf.UnknownFieldSet.newBuilder();
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            case 10: {
              if (!((mutable_bitField0_ & 0x00000001) != 0)) {
                token_ = new java.util.ArrayList<org.jiong.protobuf.TokenInfo.Token>();
                mutable_bitField0_ |= 0x00000001;
              }
              token_.add(
                  input.readMessage(org.jiong.protobuf.TokenInfo.Token.parser(), extensionRegistry));
              break;
            }
            default: {
              if (!parseUnknownField(
                  input, unknownFields, extensionRegistry, tag)) {
                done = true;
              }
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e).setUnfinishedMessage(this);
      } finally {
        if (((mutable_bitField0_ & 0x00000001) != 0)) {
          token_ = java.util.Collections.unmodifiableList(token_);
        }
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return org.jiong.protobuf.TokenPool.internal_static_org_jiong_protobuf_TokenPage_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return org.jiong.protobuf.TokenPool.internal_static_org_jiong_protobuf_TokenPage_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              org.jiong.protobuf.TokenPool.TokenPage.class, org.jiong.protobuf.TokenPool.TokenPage.Builder.class);
    }

    public static final int TOKEN_FIELD_NUMBER = 1;
    private java.util.List<org.jiong.protobuf.TokenInfo.Token> token_;
    /**
     * <code>repeated .org.jiong.protobuf.Token token = 1;</code>
     */
    public java.util.List<org.jiong.protobuf.TokenInfo.Token> getTokenList() {
      return token_;
    }
    /**
     * <code>repeated .org.jiong.protobuf.Token token = 1;</code>
     */
    public java.util.List<? extends org.jiong.protobuf.TokenInfo.TokenOrBuilder> 
        getTokenOrBuilderList() {
      return token_;
    }
    /**
     * <code>repeated .org.jiong.protobuf.Token token = 1;</code>
     */
    public int getTokenCount() {
      return token_.size();
    }
    /**
     * <code>repeated .org.jiong.protobuf.Token token = 1;</code>
     */
    public org.jiong.protobuf.TokenInfo.Token getToken(int index) {
      return token_.get(index);
    }
    /**
     * <code>repeated .org.jiong.protobuf.Token token = 1;</code>
     */
    public org.jiong.protobuf.TokenInfo.TokenOrBuilder getTokenOrBuilder(
        int index) {
      return token_.get(index);
    }

    private byte memoizedIsInitialized = -1;
    @java.lang.Override
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized == 1) return true;
      if (isInitialized == 0) return false;

      memoizedIsInitialized = 1;
      return true;
    }

    @java.lang.Override
    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      for (int i = 0; i < token_.size(); i++) {
        output.writeMessage(1, token_.get(i));
      }
      unknownFields.writeTo(output);
    }

    @java.lang.Override
    public int getSerializedSize() {
      int size = memoizedSize;
      if (size != -1) return size;

      size = 0;
      for (int i = 0; i < token_.size(); i++) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(1, token_.get(i));
      }
      size += unknownFields.getSerializedSize();
      memoizedSize = size;
      return size;
    }

    @java.lang.Override
    public boolean equals(final java.lang.Object obj) {
      if (obj == this) {
       return true;
      }
      if (!(obj instanceof org.jiong.protobuf.TokenPool.TokenPage)) {
        return super.equals(obj);
      }
      org.jiong.protobuf.TokenPool.TokenPage other = (org.jiong.protobuf.TokenPool.TokenPage) obj;

      if (!getTokenList()
          .equals(other.getTokenList())) return false;
      if (!unknownFields.equals(other.unknownFields)) return false;
      return true;
    }

    @java.lang.Override
    public int hashCode() {
      if (memoizedHashCode != 0) {
        return memoizedHashCode;
      }
      int hash = 41;
      hash = (19 * hash) + getDescriptor().hashCode();
      if (getTokenCount() > 0) {
        hash = (37 * hash) + TOKEN_FIELD_NUMBER;
        hash = (53 * hash) + getTokenList().hashCode();
      }
      hash = (29 * hash) + unknownFields.hashCode();
      memoizedHashCode = hash;
      return hash;
    }

    public static org.jiong.protobuf.TokenPool.TokenPage parseFrom(
        java.nio.ByteBuffer data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static org.jiong.protobuf.TokenPool.TokenPage parseFrom(
        java.nio.ByteBuffer data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static org.jiong.protobuf.TokenPool.TokenPage parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static org.jiong.protobuf.TokenPool.TokenPage parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static org.jiong.protobuf.TokenPool.TokenPage parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static org.jiong.protobuf.TokenPool.TokenPage parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static org.jiong.protobuf.TokenPool.TokenPage parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static org.jiong.protobuf.TokenPool.TokenPage parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }
    public static org.jiong.protobuf.TokenPool.TokenPage parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input);
    }
    public static org.jiong.protobuf.TokenPool.TokenPage parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
    }
    public static org.jiong.protobuf.TokenPool.TokenPage parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static org.jiong.protobuf.TokenPool.TokenPage parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }

    @java.lang.Override
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder() {
      return DEFAULT_INSTANCE.toBuilder();
    }
    public static Builder newBuilder(org.jiong.protobuf.TokenPool.TokenPage prototype) {
      return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
    }
    @java.lang.Override
    public Builder toBuilder() {
      return this == DEFAULT_INSTANCE
          ? new Builder() : new Builder().mergeFrom(this);
    }

    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     * Protobuf type {@code org.jiong.protobuf.TokenPage}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:org.jiong.protobuf.TokenPage)
        org.jiong.protobuf.TokenPool.TokenPageOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return org.jiong.protobuf.TokenPool.internal_static_org_jiong_protobuf_TokenPage_descriptor;
      }

      @java.lang.Override
      protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return org.jiong.protobuf.TokenPool.internal_static_org_jiong_protobuf_TokenPage_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                org.jiong.protobuf.TokenPool.TokenPage.class, org.jiong.protobuf.TokenPool.TokenPage.Builder.class);
      }

      // Construct using org.jiong.protobuf.TokenPool.TokenPage.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessageV3
                .alwaysUseFieldBuilders) {
          getTokenFieldBuilder();
        }
      }
      @java.lang.Override
      public Builder clear() {
        super.clear();
        if (tokenBuilder_ == null) {
          token_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000001);
        } else {
          tokenBuilder_.clear();
        }
        return this;
      }

      @java.lang.Override
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return org.jiong.protobuf.TokenPool.internal_static_org_jiong_protobuf_TokenPage_descriptor;
      }

      @java.lang.Override
      public org.jiong.protobuf.TokenPool.TokenPage getDefaultInstanceForType() {
        return org.jiong.protobuf.TokenPool.TokenPage.getDefaultInstance();
      }

      @java.lang.Override
      public org.jiong.protobuf.TokenPool.TokenPage build() {
        org.jiong.protobuf.TokenPool.TokenPage result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      @java.lang.Override
      public org.jiong.protobuf.TokenPool.TokenPage buildPartial() {
        org.jiong.protobuf.TokenPool.TokenPage result = new org.jiong.protobuf.TokenPool.TokenPage(this);
        int from_bitField0_ = bitField0_;
        if (tokenBuilder_ == null) {
          if (((bitField0_ & 0x00000001) != 0)) {
            token_ = java.util.Collections.unmodifiableList(token_);
            bitField0_ = (bitField0_ & ~0x00000001);
          }
          result.token_ = token_;
        } else {
          result.token_ = tokenBuilder_.build();
        }
        onBuilt();
        return result;
      }

      @java.lang.Override
      public Builder clone() {
        return super.clone();
      }
      @java.lang.Override
      public Builder setField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          java.lang.Object value) {
        return super.setField(field, value);
      }
      @java.lang.Override
      public Builder clearField(
          com.google.protobuf.Descriptors.FieldDescriptor field) {
        return super.clearField(field);
      }
      @java.lang.Override
      public Builder clearOneof(
          com.google.protobuf.Descriptors.OneofDescriptor oneof) {
        return super.clearOneof(oneof);
      }
      @java.lang.Override
      public Builder setRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          int index, java.lang.Object value) {
        return super.setRepeatedField(field, index, value);
      }
      @java.lang.Override
      public Builder addRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          java.lang.Object value) {
        return super.addRepeatedField(field, value);
      }
      @java.lang.Override
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof org.jiong.protobuf.TokenPool.TokenPage) {
          return mergeFrom((org.jiong.protobuf.TokenPool.TokenPage)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(org.jiong.protobuf.TokenPool.TokenPage other) {
        if (other == org.jiong.protobuf.TokenPool.TokenPage.getDefaultInstance()) return this;
        if (tokenBuilder_ == null) {
          if (!other.token_.isEmpty()) {
            if (token_.isEmpty()) {
              token_ = other.token_;
              bitField0_ = (bitField0_ & ~0x00000001);
            } else {
              ensureTokenIsMutable();
              token_.addAll(other.token_);
            }
            onChanged();
          }
        } else {
          if (!other.token_.isEmpty()) {
            if (tokenBuilder_.isEmpty()) {
              tokenBuilder_.dispose();
              tokenBuilder_ = null;
              token_ = other.token_;
              bitField0_ = (bitField0_ & ~0x00000001);
              tokenBuilder_ = 
                com.google.protobuf.GeneratedMessageV3.alwaysUseFieldBuilders ?
                   getTokenFieldBuilder() : null;
            } else {
              tokenBuilder_.addAllMessages(other.token_);
            }
          }
        }
        this.mergeUnknownFields(other.unknownFields);
        onChanged();
        return this;
      }

      @java.lang.Override
      public final boolean isInitialized() {
        return true;
      }

      @java.lang.Override
      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        org.jiong.protobuf.TokenPool.TokenPage parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (org.jiong.protobuf.TokenPool.TokenPage) e.getUnfinishedMessage();
          throw e.unwrapIOException();
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      private java.util.List<org.jiong.protobuf.TokenInfo.Token> token_ =
        java.util.Collections.emptyList();
      private void ensureTokenIsMutable() {
        if (!((bitField0_ & 0x00000001) != 0)) {
          token_ = new java.util.ArrayList<org.jiong.protobuf.TokenInfo.Token>(token_);
          bitField0_ |= 0x00000001;
         }
      }

      private com.google.protobuf.RepeatedFieldBuilderV3<
          org.jiong.protobuf.TokenInfo.Token, org.jiong.protobuf.TokenInfo.Token.Builder, org.jiong.protobuf.TokenInfo.TokenOrBuilder> tokenBuilder_;

      /**
       * <code>repeated .org.jiong.protobuf.Token token = 1;</code>
       */
      public java.util.List<org.jiong.protobuf.TokenInfo.Token> getTokenList() {
        if (tokenBuilder_ == null) {
          return java.util.Collections.unmodifiableList(token_);
        } else {
          return tokenBuilder_.getMessageList();
        }
      }
      /**
       * <code>repeated .org.jiong.protobuf.Token token = 1;</code>
       */
      public int getTokenCount() {
        if (tokenBuilder_ == null) {
          return token_.size();
        } else {
          return tokenBuilder_.getCount();
        }
      }
      /**
       * <code>repeated .org.jiong.protobuf.Token token = 1;</code>
       */
      public org.jiong.protobuf.TokenInfo.Token getToken(int index) {
        if (tokenBuilder_ == null) {
          return token_.get(index);
        } else {
          return tokenBuilder_.getMessage(index);
        }
      }
      /**
       * <code>repeated .org.jiong.protobuf.Token token = 1;</code>
       */
      public Builder setToken(
          int index, org.jiong.protobuf.TokenInfo.Token value) {
        if (tokenBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureTokenIsMutable();
          token_.set(index, value);
          onChanged();
        } else {
          tokenBuilder_.setMessage(index, value);
        }
        return this;
      }
      /**
       * <code>repeated .org.jiong.protobuf.Token token = 1;</code>
       */
      public Builder setToken(
          int index, org.jiong.protobuf.TokenInfo.Token.Builder builderForValue) {
        if (tokenBuilder_ == null) {
          ensureTokenIsMutable();
          token_.set(index, builderForValue.build());
          onChanged();
        } else {
          tokenBuilder_.setMessage(index, builderForValue.build());
        }
        return this;
      }
      /**
       * <code>repeated .org.jiong.protobuf.Token token = 1;</code>
       */
      public Builder addToken(org.jiong.protobuf.TokenInfo.Token value) {
        if (tokenBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureTokenIsMutable();
          token_.add(value);
          onChanged();
        } else {
          tokenBuilder_.addMessage(value);
        }
        return this;
      }
      /**
       * <code>repeated .org.jiong.protobuf.Token token = 1;</code>
       */
      public Builder addToken(
          int index, org.jiong.protobuf.TokenInfo.Token value) {
        if (tokenBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureTokenIsMutable();
          token_.add(index, value);
          onChanged();
        } else {
          tokenBuilder_.addMessage(index, value);
        }
        return this;
      }
      /**
       * <code>repeated .org.jiong.protobuf.Token token = 1;</code>
       */
      public Builder addToken(
          org.jiong.protobuf.TokenInfo.Token.Builder builderForValue) {
        if (tokenBuilder_ == null) {
          ensureTokenIsMutable();
          token_.add(builderForValue.build());
          onChanged();
        } else {
          tokenBuilder_.addMessage(builderForValue.build());
        }
        return this;
      }
      /**
       * <code>repeated .org.jiong.protobuf.Token token = 1;</code>
       */
      public Builder addToken(
          int index, org.jiong.protobuf.TokenInfo.Token.Builder builderForValue) {
        if (tokenBuilder_ == null) {
          ensureTokenIsMutable();
          token_.add(index, builderForValue.build());
          onChanged();
        } else {
          tokenBuilder_.addMessage(index, builderForValue.build());
        }
        return this;
      }
      /**
       * <code>repeated .org.jiong.protobuf.Token token = 1;</code>
       */
      public Builder addAllToken(
          java.lang.Iterable<? extends org.jiong.protobuf.TokenInfo.Token> values) {
        if (tokenBuilder_ == null) {
          ensureTokenIsMutable();
          com.google.protobuf.AbstractMessageLite.Builder.addAll(
              values, token_);
          onChanged();
        } else {
          tokenBuilder_.addAllMessages(values);
        }
        return this;
      }
      /**
       * <code>repeated .org.jiong.protobuf.Token token = 1;</code>
       */
      public Builder clearToken() {
        if (tokenBuilder_ == null) {
          token_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000001);
          onChanged();
        } else {
          tokenBuilder_.clear();
        }
        return this;
      }
      /**
       * <code>repeated .org.jiong.protobuf.Token token = 1;</code>
       */
      public Builder removeToken(int index) {
        if (tokenBuilder_ == null) {
          ensureTokenIsMutable();
          token_.remove(index);
          onChanged();
        } else {
          tokenBuilder_.remove(index);
        }
        return this;
      }
      /**
       * <code>repeated .org.jiong.protobuf.Token token = 1;</code>
       */
      public org.jiong.protobuf.TokenInfo.Token.Builder getTokenBuilder(
          int index) {
        return getTokenFieldBuilder().getBuilder(index);
      }
      /**
       * <code>repeated .org.jiong.protobuf.Token token = 1;</code>
       */
      public org.jiong.protobuf.TokenInfo.TokenOrBuilder getTokenOrBuilder(
          int index) {
        if (tokenBuilder_ == null) {
          return token_.get(index);  } else {
          return tokenBuilder_.getMessageOrBuilder(index);
        }
      }
      /**
       * <code>repeated .org.jiong.protobuf.Token token = 1;</code>
       */
      public java.util.List<? extends org.jiong.protobuf.TokenInfo.TokenOrBuilder> 
           getTokenOrBuilderList() {
        if (tokenBuilder_ != null) {
          return tokenBuilder_.getMessageOrBuilderList();
        } else {
          return java.util.Collections.unmodifiableList(token_);
        }
      }
      /**
       * <code>repeated .org.jiong.protobuf.Token token = 1;</code>
       */
      public org.jiong.protobuf.TokenInfo.Token.Builder addTokenBuilder() {
        return getTokenFieldBuilder().addBuilder(
            org.jiong.protobuf.TokenInfo.Token.getDefaultInstance());
      }
      /**
       * <code>repeated .org.jiong.protobuf.Token token = 1;</code>
       */
      public org.jiong.protobuf.TokenInfo.Token.Builder addTokenBuilder(
          int index) {
        return getTokenFieldBuilder().addBuilder(
            index, org.jiong.protobuf.TokenInfo.Token.getDefaultInstance());
      }
      /**
       * <code>repeated .org.jiong.protobuf.Token token = 1;</code>
       */
      public java.util.List<org.jiong.protobuf.TokenInfo.Token.Builder> 
           getTokenBuilderList() {
        return getTokenFieldBuilder().getBuilderList();
      }
      private com.google.protobuf.RepeatedFieldBuilderV3<
          org.jiong.protobuf.TokenInfo.Token, org.jiong.protobuf.TokenInfo.Token.Builder, org.jiong.protobuf.TokenInfo.TokenOrBuilder> 
          getTokenFieldBuilder() {
        if (tokenBuilder_ == null) {
          tokenBuilder_ = new com.google.protobuf.RepeatedFieldBuilderV3<
              org.jiong.protobuf.TokenInfo.Token, org.jiong.protobuf.TokenInfo.Token.Builder, org.jiong.protobuf.TokenInfo.TokenOrBuilder>(
                  token_,
                  ((bitField0_ & 0x00000001) != 0),
                  getParentForChildren(),
                  isClean());
          token_ = null;
        }
        return tokenBuilder_;
      }
      @java.lang.Override
      public final Builder setUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return super.setUnknownFields(unknownFields);
      }

      @java.lang.Override
      public final Builder mergeUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return super.mergeUnknownFields(unknownFields);
      }


      // @@protoc_insertion_point(builder_scope:org.jiong.protobuf.TokenPage)
    }

    // @@protoc_insertion_point(class_scope:org.jiong.protobuf.TokenPage)
    private static final org.jiong.protobuf.TokenPool.TokenPage DEFAULT_INSTANCE;
    static {
      DEFAULT_INSTANCE = new org.jiong.protobuf.TokenPool.TokenPage();
    }

    public static org.jiong.protobuf.TokenPool.TokenPage getDefaultInstance() {
      return DEFAULT_INSTANCE;
    }

    private static final com.google.protobuf.Parser<TokenPage>
        PARSER = new com.google.protobuf.AbstractParser<TokenPage>() {
      @java.lang.Override
      public TokenPage parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new TokenPage(input, extensionRegistry);
      }
    };

    public static com.google.protobuf.Parser<TokenPage> parser() {
      return PARSER;
    }

    @java.lang.Override
    public com.google.protobuf.Parser<TokenPage> getParserForType() {
      return PARSER;
    }

    @java.lang.Override
    public org.jiong.protobuf.TokenPool.TokenPage getDefaultInstanceForType() {
      return DEFAULT_INSTANCE;
    }

  }

  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_org_jiong_protobuf_TokenPage_descriptor;
  private static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_org_jiong_protobuf_TokenPage_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\017tokenPage.proto\022\022org.jiong.protobuf\032\013t" +
      "oken.proto\"5\n\tTokenPage\022(\n\005token\030\001 \003(\0132\031" +
      ".org.jiong.protobuf.TokenB\013B\tTokenPoolb\006" +
      "proto3"
    };
    descriptor = com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
          org.jiong.protobuf.TokenInfo.getDescriptor(),
        });
    internal_static_org_jiong_protobuf_TokenPage_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_org_jiong_protobuf_TokenPage_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_org_jiong_protobuf_TokenPage_descriptor,
        new java.lang.String[] { "Token", });
    org.jiong.protobuf.TokenInfo.getDescriptor();
  }

  // @@protoc_insertion_point(outer_class_scope)
}
