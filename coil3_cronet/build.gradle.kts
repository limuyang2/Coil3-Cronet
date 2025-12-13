import java.io.FileInputStream
import java.io.InputStreamReader
import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)

    `maven-publish`
    signing
}

android {
    namespace = "io.github.limuyang2.coil3.cronet"
    compileSdk = 35

    defaultConfig {
        minSdk = 21

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        jvmToolchain(17)
    }
}

dependencies {
    implementation(libs.coil)
    implementation(libs.coil.network.core)
    implementation("io.github.limuyang2:okcronet:1.0.8")
    compileOnly("org.chromium.net:cronet-api:141.7340.3")
}



//---------- maven upload info -----------------------------------

val versionName = "1.0.2"

var signingKeyId = ""//签名的密钥后8位
var signingPassword = ""//签名设置的密码
var secretKeyRingFile = ""//生成的secring.gpg文件目录

try {
    val localProperties: File = project.rootProject.file("local.properties")

    if (localProperties.exists()) {
        println("Found secret props file, loading props")
        val properties = Properties()

        InputStreamReader(FileInputStream(localProperties), Charsets.UTF_8).use { reader ->
            properties.load(reader)
        }
        signingKeyId = properties.getProperty("signing.keyId")
        signingPassword = properties.getProperty("signing.password")
        secretKeyRingFile = properties.getProperty("signing.secretKeyRingFile")

    } else {
        println("No props file, loading env vars")
    }
} catch (e: Exception) {
}


afterEvaluate {

    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components.findByName("release"))
                groupId = "io.github.limuyang2"
                artifactId = "coil3-cronet"
                version = versionName

                pom {
                    name.value("coil3-cronet")
                    description.value("A network request library.")
                    url.value("https://github.com/limuyang2/Coil3-Cronet")

                    licenses {
                        license {
                            //协议类型
                            name.value("The MIT License")
                            url.value("https://github.com/limuyang2/Coil3-Cronet/blob/main/LICENSE")
                        }
                    }

                    developers {
                        developer {
                            id.value("limuyang2")
                            name.value("limuyang")
                            email.value("limuyang2@hotmail.com")
                        }
                    }

                    scm {
                        connection.value("scm:git@github.com:limuyang2/Coil3-Cronet.git")
                        developerConnection.value("scm:git@github.com:limuyang2/Coil3-Cronet.git")
                        url.value("https://github.com/limuyang2/Coil3-Cronet")
                    }
                }
            }

        }

        repositories {
            maven {
                setUrl("$rootDir/RepoDir")
            }
        }



    }

}

gradle.taskGraph.whenReady {
    if (allTasks.any { it is Sign }) {

        allprojects {
            extra["signing.keyId"] = signingKeyId
            extra["signing.secretKeyRingFile"] = secretKeyRingFile
            extra["signing.password"] = signingPassword
        }
    }
}

signing {
    sign(publishing.publications)
}