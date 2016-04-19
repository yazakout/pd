#include <stdio.h>
#include <stdlib.h>
#include <setjmp.h>
#include <string.h>
#include <ctype.h>

/*I received help from: Johnny Colby, Yasmine Zakout, Victoria Zhou, Emma Zawila, http://www.tutorialspoint.com, http://www.cplusplus.com, http://stackoverflow.com, http://en.cppreference.com, and of course, Prof. Gheith.*/

#define MISSING() do { \
    printf("Missing code at %s:%d\n",__FILE__,__LINE__); \
    exit(0); \
} \
while(0);

/* What is this? */
static jmp_buf escape;

char *programText;

typedef struct 
{
    char *name;
    int value;
} Var;

int currName;
int numNames;
Var *names;

int get(char *id) 
{
    for (int i = 0; i < numNames; i++)
    {
        if (strcmp(id, names[i].name) == 0)
            return names[i].value;
    }
    return -1;
}

void set(char *id, int val) 
{
    printf("%s:%i\n", id, val);
    for (int i = 0; i < currName; i++)
    {
        if (strcmp(id, names[i].name) == 0)
        {
            names[i].value = val;
            return;
        }
    }
    if (currName == numNames)
    {
        names = realloc(names, numNames * 2);
        numNames = numNames * 2;
    }
    Var newName;
    newName.name = id;
    newName.value = val;
    names[currName] = newName;
    currName++;
}

static char *remaining()
{
    return programText;
}

static void error() 
{
    printf("error at '%s'\n", remaining());
    longjmp(escape, 1);
}

int isSpace();

void consume(int i) 
{
    while (isSpace())
        programText++;

    programText += i;

    while (isSpace())
        programText++;
}

int isWhile() 
{
    return (strncmp(programText, "while", 5) == 0);
}

int isIf() 
{
    return (strncmp(programText, "if", 2) == 0);
}

int isElse() 
{
    return (strncmp(programText, "else", 4) == 0);
}

int isSemi() 
{
    return programText[0] == ';';
}

int isLeftBlock() 
{
    return programText[0] == '{';
}

int isRightBlock() 
{
    return programText[0] == '}';
}

int isEq() 
{
    return programText[0] == '=' && programText[1] != '=';
}

int isEqEq() 
{
    return (strncmp(programText, "==", 2) == 0);
}

int isLeft() 
{
    return programText[0] == '(';
}

int isRight() 
{
    return programText[0] == ')';
}

int isEnd() 
{
    return programText[0] == 0;
}

int isSpace() 
{
    return programText[0] == ' ';
}

int isId() 
{
    int index = 0;
    while (('a' <= programText[index] && programText[index] <= 'z') 
        || (('0' <= programText[index] && programText[index] <= '9') && index != 0))
    {
        index++;
    }

    return !((index == 0)
        || (((index == 4) && strncmp(programText, "else", 4) == 0)
        || ((index == 5) && strncmp(programText, "while", 5) == 0)
        || ((index == 2) && (strncmp(programText, "if", 2) == 0))));
}

int isMul() 
{
    return programText[0] == '*';
}

int isPlus() 
{
    return programText[0] == '+';
}

char *getId() 
{
    int index = 0;
    while (('a' <= programText[index] && programText[index] <= 'z')
        || (('0' <= programText[index] && programText[index] <= '9') && index != 0))
    {
        index++;
    }
    char *name = (char*)(malloc((index + 1) * sizeof(char)));
    strncpy(name, programText, index);
    name[index + 1] = 0; 
    if (!(strncmp(name, "else", 4) || strncmp(name, "while", 5) || strncmp(name, "if", 2)))
        return 0;
    return name;
}

int isInt() 
{
    return ('0' <= programText[0] && programText[0] <= '9');
}

int getInt() 
{
    int currentNum = -1;
    int index = 0;
    while(('0' <= programText[index] && programText[index] <= '9') || programText[index] == '_')
    {
        if ('0' <= programText[index] && programText[index] <= '9')
        {
            if (currentNum < 0)
                currentNum = programText[index] - '0';
            else
                currentNum = currentNum * 10 + (programText[index] - '0');
        }
        index++;
    }
    return currentNum;
}

int getIntSpace()
{
    int spaces = 0;
    int index = 0;
    while(('0' <= programText[index] && programText[index] <= '9') || programText[index] == '_')
    {
        spaces++;
        index++;
    }
    return spaces;
}
/* Forward declarations */
int expression();
void seq(int value);

/* handle id, literals, and (...) */
int e1() 
{
    if (isLeft()) 
    {
        consume(1);
        int v = expression();
        if (!isRight()) 
        {
            error();
        }
        consume(1);
        return v;
    } 
    else if (isInt()) 
    {
        int v = getInt();
        int spaces = getIntSpace();
        consume(spaces);

        return v;
    } 
    else if (isId()) 
    {
        char *id = getId();
        consume(strlen(id));
        return get(id);
    } 
    else 
    {
        error();
        return 0;
    }
}

/* handle '*' */
int e2() 
{
    int value = e1();
    while (isMul()) 
    {
        consume(1);
        value = value * e1();
    }
    return value;
}

/* handle '+' */
int e3() 
{
    int value = e2();
    while (isPlus()) 
    {
       consume(1);
        value = value + e2();
    }
    return value;
}

/* handle '==' */
int e4() 
{
    int value = e3();
    while (isEqEq()) 
    {
        consume(2);
        value = value == e3();
    }
    return value;
}

int expression() 
{
    return e4();
}

int statement(int value) 
{
    if (isId()) 
    {
        char *id = getId();
        consume(strlen(id));
        if (!isEq())
            error();
        consume(1);
        int v = expression();
        if (value)
            set(id, v);

        if (isSemi()) 
        {
            consume(1);
        }

        return 1;
    } 
    else if (isLeftBlock()) 
    {
        consume(1);
        seq(value);
        if (!isRightBlock())
            error();
        consume(1);
        return 1;
    } 
    else if (isIf()) 
    {
        consume(2);

        int c = expression();
        if (c && value)
            statement(1);
        else
            statement(0);

        if (isElse()) 
        {
            consume(4);

            if (!c && value)
                statement(1);
            else
                statement(0);
        }
        return 1;
    } 
    else if (isWhile()) 
    {
        /* Implement while */
        consume(5);
        
        char *progTextCopy;
        progTextCopy = malloc(sizeof(char) * strlen(programText));
        strcpy(progTextCopy, programText);

        int c;
        do
        {
            programText = progTextCopy;
            c = expression();
            if (c)
                statement(1);
            else
                statement(0);
        }
        while (c && value);

        return 1;
    } 
    else if (isSemi()) 
    {
        consume(1);
        return 1;
    }
    else 
    {
        return 0;
    }
}

void seq(int value) 
{
    while (statement(value)) ;
}

void program() 
{
    seq(1);
    if (!isEnd())
        error();
}

void interpret(char *prog) 
{
    /* initialize global variables */
    programText = prog;
    numNames = 20;
    names = (Var*)(malloc(sizeof(Var) * 10));
    currName = 0;
    
    int x = setjmp(escape);
    if (x == 0) 
    {
        program();
    }
}

int main(int argc, char *argv[]) 
{
    interpret(argv[1]);
    free(names);
    return 0;
}
