package com.morkato.bmt.annotation;

import com.morkato.bmt.Extension;

public @interface MorkatoComponent {
  Class<? extends Extension> extension() default null;
}
