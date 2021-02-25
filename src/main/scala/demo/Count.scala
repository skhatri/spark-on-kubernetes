package demo

import com.datastax.oss.driver.api.core.CqlSessionBuilder
import org.apache.spark.sql.SparkSession

object Count extends App {

  import org.apache.hadoop.security.UserGroupInformation

  UserGroupInformation.setLoginUser(UserGroupInformation.createRemoteUser("root"))

  val builder = new CqlSessionBuilder().withAuthCredentials("cassandra", "cassandra")
  println(s"show builder ${builder}")

  val master = Option(System.getenv("SPARK_MASTER"))
    .getOrElse("local[1]")
  val spark = SparkSession.builder().appName("hello-app")
    .master(master)
    .getOrCreate()
  spark.range(10).show(5, false)
  spark.close()
}
