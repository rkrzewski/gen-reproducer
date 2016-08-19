Error reporoducer project for a problem with shapeless.Generic derivation
-------------------------------------------------------------------------

Metrics.scala contains definitions of a sealed trait hierarchy and a companion 
object of the top level trait contains a `val generic = Generic[Metric]` 
defintion.

LineProtocol.scala contains functions that operate on said type hierarchy. 
However refering to `Metric.generic` value from one of this function causes 
a compilation error in Metrics.scala

This makes me suspect that I might have run into a scalac error.

Side node: those metrics are https://github.com/dropwizard/metrics representation,
and "line protocol" is https://docs.influxdata.com/influxdb/v0.13/write_protocols/line/
