package me.telesphoreo.electrum.command;

import me.telesphoreo.electrum.rank.Rank;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CommandParameters
{

    String description();

    String usage() default "/<command>";

    String aliases() default ""; // alias1, alias2, alias3 etc

    Rank rank();

    SourceType source();
}
