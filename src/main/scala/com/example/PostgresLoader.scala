package com.example

import java.sql.DriverManager
import java.util
import java.util.Properties
import java.util.function.Consumer

import org.apache.spark.api.java.function.ForeachPartitionFunction
import org.postgresql.PGConnection

class PostgresLoader extends ForeachPartitionFunction[(Int, String)] {

  override def call(t: util.Iterator[(Int, String)]): Unit = {
    val properties = new Properties()
    properties.setProperty("user", "infare")
    properties.setProperty("password", "infare")
    properties.setProperty("driver", "org.postgresql.Driver")
    val url = "jdbc:postgresql://localhost:5433/infare_test_task"
    val tableName = "task_output"
    val pGConnection = DriverManager.getConnection(url, properties).asInstanceOf[PGConnection]
    val copyIn = pGConnection.getCopyAPI.copyIn(s"COPY $tableName FROM STDIN WITH DELIMITER ','")
    t.forEachRemaining(new Consumer[(Int, String)] {
      override def accept(t: (Int, String)): Unit = {
        val toCopy = t._2.substring(1, t._2.length - 1)
        copyIn.writeToCopy(toCopy.getBytes(), 0, toCopy.getBytes.length)
      }
    })
    copyIn.endCopy()
  }
}
