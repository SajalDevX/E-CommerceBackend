package example.com.mappers


import example.com.dao.products.product.entity.ProductEntity
import example.com.dao.users.entity.UserDetails
import example.com.dao.users.entity.UserEntity
import example.com.model.SignUpParams
import example.com.model.UpdateProduct
import example.com.model.UserDetailsParams
import example.com.security.hashPassword

fun SignUpParams.toUserEntity() =
    UserEntity(
        name = name,
        email = email,
        password = hashPassword(password),
        userDetails = userDetailsParams.toUserDetails(),
    )

fun UserDetailsParams.toUserDetails() =
    UserDetails(
        age = age,
        mobile = mobile,
        gender = gender,
        userRole = userRole
    )

fun ProductEntity.toUpdateProduct() =
    UpdateProduct(
        categoryId, subCategoryId, brandId, productName, productCode, productQuantity, productDetail, price, discountPrice, videoLink, hotDeal, buyOneGetOne, imageOne, imageTwo
    )