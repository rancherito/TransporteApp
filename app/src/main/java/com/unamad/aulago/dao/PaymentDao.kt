package com.unamad.aulago.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.unamad.aulago.models.database.PaymentModel

@Dao
interface PaymentDao {

    @Delete
    fun delete(payments: List<PaymentModel>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(studentSectionModel: List<PaymentModel>)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    fun update(studentSectionModel: List<PaymentModel>)

    @Query(
        """
        SELECT P.* FROM Payments P
        INNER JOIN SystemData SY ON SY.currentUserId = P.userId
    """
    )
    fun list(): List<PaymentModel>

    @Query(
        """
        SELECT P.* FROM Payments P
        INNER JOIN SystemData SY ON SY.currentUserId = P.userId
    """
    )
    fun listStream(): LiveData<List<PaymentModel>>
}