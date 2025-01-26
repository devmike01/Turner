# Turner
### How to
##### Step 1. Add the JitPack repository to your build file
Add it in your project's root `build.gradle` 
```groovy
	dependencyResolutionManagement {
		repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
		repositories {
			mavenCentral()
			maven { url 'https://jitpack.io' }
		}
	}
```
If your gradle file is written in Kotlin(i.e `build.gradle`), use this
```kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven(url ="https://jitpack.io")
    }

}
```
##### Step 2. Add `Turner` to your project
```groovy
dependencies {
	        implementation 'com.github.devmike01:Turner:0.1.1-alpha01'
	}
```
That's it!

### Demo
<img src="media/demo.gif"
alt="Devmike01's Turner demo" width="220" height="400" />

### Contributions
Feel free to submit PRs

### Known issue(s)
- Turner's state is lost when device's screen is rotated
