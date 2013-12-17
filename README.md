# tweet-summary

This is fun project to explore a few concepts in clojure. 

It retrieves tweets of the given users using http-kit asynchronously. 

The http-kit operations are wrapped in go blocks, which provides a good case for using go channells. 

The retrieved tweets are then processed parallelly. 

## License

Copyright © 2013 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
