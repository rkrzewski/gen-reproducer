package metrics

import shapeless._

object LineProtocol {

  object fieldToString extends Poly1 {
    implicit def caseLong = at[Long](x => x.toString + "i")
    implicit def caseDouble = at[Double](x => x.toString)
    implicit def caseBigDecimal = at[BigDecimal](x => x.toString)
    implicit def caseString = at[String](x => "\"" + x.replaceAll("\"", "\\\\\"") + "\"")
    implicit def caseStringSeq = at[Seq[String]](x => "\"" + x.mkString(",").replaceAll("\"", "\\\\\"") + "\"")
  }

  object metricToFields extends Poly1 {
    implicit def caseNumericGauge = at[NumericGauge](x => Seq(
      ('value, fieldToString(x.value))))
    implicit def caseStringsGauge = at[StringsGauge](x => Seq(
      ('value, fieldToString(x.value))))
    implicit def caseFailedGauge = at[FailedGauge](x => Seq(
      ('error, fieldToString(x.error))))
    implicit def caseCounter = at[Counter](x => Seq(
      ('count, fieldToString(x.count))))
    implicit def caseHistogram = at[Histogram](x => Seq(
      ('count, fieldToString(x.count)),
      ('min, fieldToString(x.min)),
      ('max, fieldToString(x.max)),
      ('mean, fieldToString(x.mean)),
      ('stddev, fieldToString(x.stddev)),
      ('p50, fieldToString(x.p50)),
      ('p75, fieldToString(x.p75)),
      ('p95, fieldToString(x.p95)),
      ('p98, fieldToString(x.p98)),
      ('p99, fieldToString(x.p99)),
      ('p999, fieldToString(x.p999))))
    implicit def caseMeter = at[Meter](x => Seq(
      ('count, fieldToString(x.count)),
      ('m1_rate, fieldToString(x.m1_rate)),
      ('m5_rate, fieldToString(x.m5_rate)),
      ('m15_rate, fieldToString(x.m15_rate)),
      ('mean_rate, fieldToString(x.mean_rate)),
      ('units, fieldToString(x.units))))
    implicit def caseTimer = at[Timer](x => Seq(
      ('count, fieldToString(x.count)),
      ('min, fieldToString(x.min)),
      ('max, fieldToString(x.max)),
      ('mean, fieldToString(x.mean)),
      ('stddev, fieldToString(x.stddev)),
      ('p50, fieldToString(x.p50)),
      ('p75, fieldToString(x.p75)),
      ('p95, fieldToString(x.p95)),
      ('p98, fieldToString(x.p98)),
      ('p99, fieldToString(x.p99)),
      ('p999, fieldToString(x.p999)),
      ('m1_rate, fieldToString(x.m1_rate)),
      ('m5_rate, fieldToString(x.m5_rate)),
      ('m15_rate, fieldToString(x.m15_rate)),
      ('mean_rate, fieldToString(x.mean_rate)),
      ('duration_units, fieldToString(x.duration_units)),
      ('rate_units, fieldToString(x.rate_units))))
  }

  def fieldsToString(fields: Seq[(Symbol, String)]): String =
    fields.map {
      case (Symbol(field), value) => s"$field=$value"
    }.mkString(",")

  // uncommenting the body of the method below, causes compilation error: 
  // gen-minimizer/src/main/scala/metrics/Metrics.scala:29: could not find implicit value for parameter gen: shapeless.Generic[metrics.Metric]

  def metricMapToString[T <: Metric](prefix: String, map: Map[String, T], tags: String, timestamp: String): String = ???
  //    map.toSeq.map {
  //      case (key, metric) =>
  //        val fields = Metric.generic.to(metric).map(metricToFields).unify
  //        s"${prefix}.${key}${tags} ${fieldsToString(fields)} ${timestamp}"
  //    }.mkString("\n")

  def metricsToMaps(metrics: Metrics): Seq[(Symbol, Map[String, Metric])] = Seq(
    ('gauges, metrics.gauges),
    ('counters, metrics.counters),
    ('histograms, metrics.histograms),
    ('meters, metrics.meters),
    ('timers, metrics.timers))

  def metricsToString(metrics: Metrics, tags: String, timestamp: String): String =
    metricsToMaps(metrics).map {
      case (Symbol(field), map) =>
        metricMapToString(field, map, tags, timestamp)
    }.filter(_.length() > 0).mkString("\n")
}