package com.example

import org.apache.spark.SparkConf
import org.apache.spark.sql.{Encoder, Encoders, SparkSession}

object Main {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("Infare test")
      //      .set("spark.driver.maxResultSize", "2048")
      .setMaster("local[*]")
    val spark = SparkSession.builder().config(conf).getOrCreate()
    implicit val encoder: Encoder[HiveModel] = Encoders.product[HiveModel]
    import spark.implicits._
    val df = spark.read
      //      .schema(StructType(StructField("observed_date_min_as_infaredate", IntegerType, nullable = false)
      //        :: StructField("observed_date_max_as_infaredate", IntegerType, nullable = false)
      //        :: StructField("full_weeks_before_departure", IntegerType, nullable = false)
      //        :: StructField("carrier_id", IntegerType, nullable = false)
      //        :: StructField("searched_cabin_class", StringType, nullable = false)
      //        :: StructField("booking_site_id", IntegerType, nullable = false)
      //        :: StructField("booking_site_type_id", IntegerType, nullable = false)
      //        :: StructField("is_trip_one_way", IntegerType, nullable = false)
      //        :: StructField("trip_origin_airport_id", IntegerType, nullable = false)
      //        :: StructField("trip_destination_airport_id", IntegerType, nullable = false)
      //        :: StructField("trip_min_stay", IntegerType, nullable = false)
      //        :: StructField("trip_price_min", DoubleType, nullable = false)
      //        :: StructField("trip_price_max", DoubleType, nullable = false)
      //        :: StructField("trip_price_avg", DoubleType, nullable = false)
      //        :: StructField("aggregation_count", IntegerType, nullable = false)
      //        :: StructField("out_flight_departure_date_as_infaredate", IntegerType, nullable = false)
      //        :: StructField("out_flight_departure_time_as_infaretime", IntegerType, nullable = false)
      //        :: StructField("out_flight_time_in_minutes", IntegerType, nullable = false)
      //        :: StructField("out_sector_count", IntegerType, nullable = false)
      //        :: StructField("out_flight_sector_1_flight_code_id", IntegerType, nullable = false)
      //        :: StructField("out_flight_sector_2_flight_code_id", IntegerType, nullable = false)
      //        :: StructField("out_flight_sector_3_flight_code_id", IntegerType, nullable = false)
      //        :: StructField("home_flight_departure_date_as_infaredate", IntegerType, nullable = false)
      //        :: StructField("home_flight_departure_time_as_infaretime", IntegerType, nullable = false)
      //        :: StructField("home_flight_time_in_minutes", IntegerType, nullable = false)
      //        :: StructField("home_sector_count", IntegerType, nullable = false)
      //        :: StructField("home_flight_sector_1_flight_code_id", IntegerType, nullable = false)
      //        :: StructField("home_flight_sector_2_flight_code_id", IntegerType, nullable = false)
      //        :: StructField("home_flight_sector_3_flight_code_id", IntegerType, nullable = false)
      //        :: Nil))
      .option("sep", "\t")
      .csv("/tmp/infare")
      .map(row => (row.getAs[String](0).toInt / 7, row.toString))
      //      .map(row => (row.getAs[String](0).toInt / 7, new HiveModel(row.toSeq.map(String.valueOf).toArray)))
      .foreachPartition(new PostgresLoader)

    spark.stop()
    // COPY to postgresql (either into different tables and append them as partitions or into single table)
  }
}
