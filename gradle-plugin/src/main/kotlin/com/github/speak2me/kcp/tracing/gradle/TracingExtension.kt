package com.github.speak2me.kcp.tracing.gradle

import javax.inject.Inject
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property

public abstract class TracingExtension @Inject constructor(objects: ObjectFactory) {

  public val annotation: Property<String> = objects.property(String::class.java)

}
