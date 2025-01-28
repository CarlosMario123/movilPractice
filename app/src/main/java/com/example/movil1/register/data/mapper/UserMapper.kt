import com.example.movil1.register.data.model.CreateUserRequest
import com.example.movil1.register.data.model.UserDTO
import com.example.movil1.register.domain.model.User

object UserMapper {
    fun UserDTO.toDomain(): User {
        return User(
            nombre = this.nombre,
            email = this.email
        )
    }

    fun User.toCreateUserRequest(password: String): CreateUserRequest {
        return CreateUserRequest(
            nombre = this.nombre,
            email = this.email,
            password = password
        )
    }
}



