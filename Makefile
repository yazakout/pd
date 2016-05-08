TESTS=$(sort $(wildcard *.test))
RUNS=$(patsubst %.test,%.run,$(TESTS))

test : $(RUNS)

$(RUNS) : %.run : %.test Makefile pd.scala
	echo "[$*] \"`cat $*.test`\" ... "
	scala pd.scala "`head -1 $*.test`" > $*.out
	((diff -b $*.out $*.ok > /dev/null) && echo "pass") || (echo "fail" ; echo "--- expected ---"; cat $*.ok; echo "--- found ---" ; cat $*.out)

clean :
	rm -f *.d
	rm -f *.o
	rm -f pd

-include *.d
