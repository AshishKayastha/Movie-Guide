package com.ashish.movieguide.utils

import java.io.IOException

class NoCacheException : IOException("Couldn't find cached response for the given request.")