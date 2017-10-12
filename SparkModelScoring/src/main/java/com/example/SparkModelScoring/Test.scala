package com.example.SparkModelScoring

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.mllib.recommendation.{MatrixFactorizationModel, Rating, ALS}

/**
  * Created by bhupesh on 12/10/17.
  */
class Test {

  def Test()
  {
  }

  def testModel()
  {
    val conf = new SparkConf().setAppName("Simple Application").setMaster("local")
    val sc = new SparkContext(conf)

    val data = sc.textFile("/tmp/ml-latest-small/newrating")
    val ratings = data.map(_.split(',') match { case Array(user, item, rate) =>
      Rating(user.toInt, item.toInt, rate.toDouble)
    })

    // Build the recommendation model using ALS
    val rank = 10
    val numIterations = 10
    val model = ALS.train(ratings, rank, numIterations, 0.01)

    // Evaluate the model on rating data
    val usersProducts = ratings.map { case Rating(user, product, rate) =>
      (user, product)
    }
    val predictions =
      model.predict(usersProducts).map { case Rating(user, product, rate) =>
        ((user, product), rate)
      }
    val ratesAndPreds = ratings.map { case Rating(user, product, rate) =>
      ((user, product), rate)
    }.join(predictions)
    val MSE = ratesAndPreds.map { case ((user, product), (r1, r2)) =>
      val err = (r1 - r2)
      err * err
    }.mean()
    println("Mean Squared Error = " + MSE)

    // Save and load model
    model.save(sc, "/tmp/ml-latest-small/myCollaborativeFilter")
    val sameModel = MatrixFactorizationModel.load(sc, "/tmp/ml-latest-small/myCollaborativeFilter")
    println("Read Model " + sameModel)

  }
}
