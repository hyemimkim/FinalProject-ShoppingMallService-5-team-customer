package com.judamie_user.android.firebase.repository

import android.net.Uri
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.judamie_user.android.firebase.model.ProductModel
import com.judamie_user.android.firebase.vo.ProductVO
import com.judamie_user.android.util.ProductCategory
import com.judamie_user.android.util.ProductState
import kotlinx.coroutines.tasks.await

class ProductRepository {

    companion object {
        // 제품 목록을 가져오는 메서드
        suspend fun gettingProductList(productCategory: ProductCategory): MutableList<Map<String, *>> {
            val firestore = FirebaseFirestore.getInstance()
            // TODO (change collection name : productData -> ProductData)
            val collectionReference = firestore.collection("productData")
            val result = if (productCategory == ProductCategory.PRODUCT_CATEGORY_DEFAULT) {
                collectionReference.orderBy("productTimeStamp", Query.Direction.DESCENDING).get()
                    .await()

            } else {
                collectionReference.whereEqualTo("productCategory", productCategory.str)
                    .orderBy("productTimeStamp", Query.Direction.DESCENDING).get().await()

            }
            // 반환할 리스트
            val resultList = mutableListOf<Map<String, *>>()
            // 데이터의 수 만큼 반환한다.
            result.forEach {
                val map = mapOf(
                    // 제품의 Document Id
                    "documentId" to it.id,
                    // 데이터를 가지고 있는 객체
                    "productVO" to it.toObject(ProductVO::class.java)
                )
                resultList.add(map)
            }


            return resultList
        }

//        // userCartList에 포함된 상품 ID를 기반으로 상품 정보를 가져오기
//        suspend fun gettingCartList(productIds: List<String>): List<ProductModel> {
//            val productList = mutableListOf<ProductModel>()
//            val productRef = FirebaseFirestore.getInstance().collection("productData")
//
//            productIds.forEach { id ->
//                val document = productRef.document(id).get().await()
//                if (document.exists()) {
//                    // DocumentSnapshot에서 직접 데이터 추출
//                    val data = document.data
//                    if (data != null) {
//                        val productModel = ProductModel()
//
//                        productModel.productDocumentId = data["productDocumentId"] as? String ?: ""
//                        productModel.productName = data["productName"] as? String ?: ""
//                        productModel.productDiscountRate = (data["productDiscountRate"] as? Long)?.toInt() ?: 0
//                        productModel.productPrice = (data["productPrice"] as? Long)?.toInt() ?: 0
//                        productModel.productMainImage = data["productMainImage"].toString()
//
//                        // productState를 Long으로 가져와서 enum으로 변환
//                        val productStateLong = data["productState"] as? Long ?: 1L
//                        productModel.productState =
//                            ProductState.fromNumber(productStateLong.toInt())
//
//                        productList.add(productModel)
//                    }
//                }
//
//            }
//            return productList
//        }

        suspend fun gettingCartList(productIds: List<String>): List<ProductModel> {
            val productList = mutableListOf<ProductModel>()
            val productRef = FirebaseFirestore.getInstance().collection("productData")

            productIds.forEach { id ->
                val document = productRef.document(id).get().await()
                if (document.exists()) {
                    // DocumentSnapshot에서 데이터 추출
                    val data = document.data
                    if (data != null) {
                        val productModel = ProductModel()

                        // Firestore 문서 ID를 productDocumentId로 설정
                        productModel.productDocumentId = document.id

                        productModel.productName = data["productName"] as? String ?: ""
                        productModel.productDiscountRate = (data["productDiscountRate"] as? Long)?.toInt() ?: 0
                        productModel.productPrice = (data["productPrice"] as? Long)?.toInt() ?: 0
                        productModel.productMainImage = data["productMainImage"].toString()
                        productModel.productStock = (data["productStock"] as? Long)?.toInt() ?: 0

                        // productState를 Long으로 가져와서 enum으로 변환
                        val productStateLong = data["productState"] as? Long ?: 1L
                        productModel.productState = ProductState.fromNumber(productStateLong.toInt())

                        // 로그로 각 필드 출력 (toString()을 사용하지 않음)
                        Log.d("ShopCartFragment", "Product: ProductDocumentId=${productModel.productDocumentId}, " +
                                "ProductName=${productModel.productName}, ProductDiscountRate=${productModel.productDiscountRate}, " +
                                "ProductPrice=${productModel.productPrice}, ProductMainImage=${productModel.productMainImage}, " +
                                "ProductState=${productModel.productState}")

                        productList.add(productModel)
                    }
                }
            }
            return productList
        }



        // 이미지 데이터를 가져온다.
        suspend fun gettingImage(imageFileName: String): Uri {
            val storageReference = FirebaseStorage.getInstance().reference
            // 파일명을 지정하여 이미지 데이터를 가져온다.
            val childStorageReference = storageReference.child("image/$imageFileName")
            val imageUri = childStorageReference.downloadUrl.await()
            return imageUri
        }
    }
}
