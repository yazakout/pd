CFLAGS=-g -std=c99 -O0 -Werror -Wall

pd.scala : Makefile
	scalac pd.scala

TESTS=$(sort $(wildcard *.test))
RUNS=$(patsubst %.test,%.run,$(TESTS))

test : $(RUNS)

$(RUNS) : %.run : %.test Makefile pd.scala
	@echo -n "[$*] \"`cat $*.test`\" ... "
	@-scala pd.scala $*.test`" > $*.out
	@((diff -b $*.out $*.ok > /dev/null) && echo "pass") || (echo "fail" ; echo "--- expected ---"; cat $*.ok; echo "--- found ---" ; cat $*.out)

clean :
	rm -f *.d
	rm -f *.o
	rm -f pd

-include *.d
