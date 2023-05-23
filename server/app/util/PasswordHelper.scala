package util

import java.security.SecureRandom
import java.util.Base64
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

object PasswordHelper {

  private val Iteration = 65536
  private val KeyLength = 256
  private val Algorithm = "PBKDF2WithHmacSHA256"
  private val SaltSize = 128
  private val Random = new SecureRandom()

  private def generateHash(password: String, salt: Array[Byte]): Array[Byte] = {
    val keySpec = new PBEKeySpec(password.toCharArray, salt, Iteration, KeyLength)
    val keyFactory = SecretKeyFactory.getInstance(Algorithm)
    keyFactory.generateSecret(keySpec).getEncoded
  }

  def generateSaltAndHash(password: String): (String, String) = {
    val salt = new Array[Byte](SaltSize)
    Random.nextBytes(salt)
    val hash = generateHash(password, salt)
    (Base64.getEncoder.encodeToString(salt), Base64.getEncoder.encodeToString(hash))
  }

  def isValidPassword(password: String, salt: String, hash: String): Boolean = {
    val saltBytes = Base64.getDecoder.decode(salt)
    val hashBytes = Base64.getDecoder.decode(hash)
    val calculated = generateHash(password, saltBytes)
    calculated.sameElements(hashBytes)
  }
}
