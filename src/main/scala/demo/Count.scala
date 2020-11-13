package demo

import org.apache.spark.sql.SparkSession

object Count extends App {

  import org.apache.hadoop.security.UserGroupInformation

  UserGroupInformation.setLoginUser(UserGroupInformation.createRemoteUser("root"))

  val master = Option(System.getenv("SPARK_MASTER"))
    .getOrElse("local[1]")
  val spark = SparkSession.builder().appName("hello-app")
    .master(master)
    .getOrCreate()
  spark.range(10).show(5, false)
  spark.close()
}
