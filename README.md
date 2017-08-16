# shen-truffle

shen-truffle is a port of the [Shen programming
language](http://www.shenlanguage.org/) to the [Graal
runtime](https://github.com/graalvm/graal).

### Acknowledgments

Thanks to [Dr Mark Tarver](http://marktarver.com) for his work in
science and philosophy in general, and Shen in particular.

## Usage

Not yet 

## Development

You will need:

- A GraalVM (< 0.26) or a JDK8 built with JVMCI. You can download either from:
  - http://www.oracle.com/technetwork/oracle-labs/program-languages/downloads/index.html
- [Maven](https://maven.apache.org/)

To build:

```
mvn compile
```

To run you will currently need to hack the `shen-truffle` script to use your GraalVM/JVM.

## References

### Shen

- http://www.shenlanguage.org/
- http://www.shenlanguage.org/learn-shen/shendoc.htm
- [Shen Sources](https://github.com/shen-Language/shen-sources)

### Other Shen ports
- [Shen Common Lisp](https://github.com/shen-Language/shen-cl)
- [Shen.java](https://github.com/hraberg/Shen.java)
- [ShenSharp](https://github.com/rkoeninger/ShenSharp)

### General

- http://matt.might.net/articles/closure-conversion/


### Truffle
- [Graal/truffle](https://github.com/graalvm/graal/truffle)
- [Truffle Javadoc](https://graalvm.github.io/graal/truffle/javadoc/)

### Other truffle languages
- [TruffleRuby](https://github.com/graalvm/truffleruby)
- [An Oz Implementation using Truffle and Graal](https://dial.uclouvain.be/memoire/ucl/en/object/thesis%3A10657/datastream/PDF_01/view)
- [TruffleClojure](http://ssw.jku.at/Teaching/MasterTheses/Graal/TruffleClojure.pdf)
- [Mumbler](http://cesquivias.github.io/tags/truffle.html)

