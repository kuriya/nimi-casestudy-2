package com.nimi.guildbank.transformer;

public interface Transformer<S, T> {
    T transform(S source);
}
