package com.godx.annoprocessor.retrofit;

import com.godx.annoprocessor.BaseProcessor;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Element;

public class RepositoryFactory {
    private TypeSpec repository;
    public RepositoryFactory(Builder builder) {

    }

    public void createRepository() {

    }

    public static class Builder {
        private final Element element;
        private String packageName;

        public Builder(Element element) {
            this.element = element;
        }

        public RepositoryFactory build() {
            return new RepositoryFactory(this);
        }

        public Builder packageName(String packageName) {
            this.packageName = packageName;
            return this;
        }
    }
}
