package com.ashish.movieguide.utils

import java.lang.RuntimeException

class AuthException : RuntimeException("Unauthorized User. You need to log in for this feature.")