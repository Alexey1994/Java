/*
    NOT не работает

          IP - счётчик комманд
          SB - стоп-бит

    CMP:  сравнение, результат разности значений помещается в ZF

          JE     Переход, если равно/нуль
          JNE    Переход, если не равно/не нуль
          JG     Переход, если больше/не меньше или равно
          JGE    Переход, если больше или равно/не меньше
          JL     Переход, если меньше/не больше или равно
          JLE    Переход, если меньше или равно/не больше

    INT:  "прерывания"

          PRINT - печать по адресу из BX до символа нуля
          PRINTSHORT - печать числа из AX
          PAUSE - пауза до нажатия ENTER
          STOP

    NOP - ничего не делает

    INC,DEC - инкремент, декремент

    SHR,SHL - побитовый сдвиг

    CALL,RET - соответственно вызов, возврат из подпрограммы

    POP - извлекает в AX
*/

public class Intel8086
{
    private short data[];

    private Stack s, cs;  //cs-стек подпрограмм, s-данных

    private final short NOP=0,
                        INT=1,
                        MOV=2,
                        MOV_AX_BX=3,
                        MOV_BX_AX=4,
                        MOV_BX_BX=5,
                        MOV_AX=6,
                        MOV_BX=7,
                        MOV_MEM_AX=8,
                        MOV_MEM_BX=9,
                        MOV_MEM_MEM=10,

                        INC=11,
                        INC_AX=12,
                        INC_BX=13,
                        DEC=14,
                        DEC_AX=15,
                        DEC_BX=16,

                        ADD=17,
                        SUB=18,
                        DIV=19,
                        MUL=20,
                        NEG=21,

                        SHR=22,
                        SHL=23,

                        OR=24,
                        AND=25,
                        XOR=26,
                        NOT=27,

                        CMP=28,
                        JE=29,
                        JNE=30,
                        JG=31,
                        JGE=32,
                        JL=33,
                        JLE=34,
                        JMP=35,

                        PUSH=36,
                        PUSH_AX=37,
                        PUSH_BX=38,
                        POP=39,
                        CALL=40,
                        RET=41;

    private final short PRINT=42,
                        PRINTSHORT=43,
                        PAUSE=44,
                        STOP=45;

    private short IP,

                  AX,
                  BX;

    private short ZF;

    private boolean SB;

    public Intel8086()
    {
        data=new short[10000];
        s=new Stack();
        cs=new Stack();
    }

    public Intel8086(short[] d)
    {
        data=d;
        s=new Stack();
        cs=new Stack();
    }

    public short[] get_data()
    {
        return data;
    }

    public void run()
    {
        while(!SB && IP<10000)
        {
            switch(data[IP])
            {
                case NOP: IP++; break;
                case INT: interrupt(); IP++; break;

                case MOV: data[IP+1]=data[IP+2]; IP+=3; break;
                case MOV_AX: AX=data[IP+1]; IP+=2; break;
                case MOV_BX: BX=data[IP+1]; IP+=2; break;
                case MOV_AX_BX: AX=data[BX]; IP++; break;
                case MOV_BX_AX: BX=AX; IP++; break;
                case MOV_BX_BX: BX=data[BX]; IP++; break;
                case MOV_MEM_AX: data[IP+1]=AX; IP+=2; break;
                case MOV_MEM_BX: data[IP+1]=data[BX]; IP+=2; break;
                case MOV_MEM_MEM: data[IP+1]=data[IP+2]; IP+=3; break;

                case INC: data[IP+1]++; IP+=2; break;
                case INC_AX: AX++; IP++; break;
                case INC_BX: BX++; IP++; break;
                case DEC: data[IP+1]--; IP+=2; break;
                case DEC_AX: AX--; IP++; break;
                case DEC_BX: BX--; IP++; break;

                case ADD: AX+=data[IP+1]; IP+=2; break;
                case SUB: AX-=data[IP+1]; IP+=2; break;
                case MUL: AX*=data[IP+1]; IP+=2; break;
                case DIV: AX/=data[IP+1]; IP+=2; break;
                case NEG: AX*=-1; IP++;break;

                case SHL: AX<<=data[IP+1]; IP+=2; break;
                case SHR: AX>>=data[IP+1]; IP+=2; break;

                case OR: AX|=data[IP+1]; IP+=2; break;
                case AND: AX&=data[IP+1]; IP+=2; break;
                case XOR: AX^=data[IP+1]; IP+=2; break;
                //case NOT: AX=!AX; break;

                case CMP: ZF=data[IP+1]; ZF-=data[IP+2]; IP+=3; break;

                case JE: if(ZF==0) IP=data[IP+1]; break;
                case JNE: if(ZF!=0) IP=data[IP+1]; break;
                case JG: if(ZF>0) IP=data[IP+1]; break;
                case JGE: if(ZF>=0) IP=data[IP+1]; break;
                case JL: if(ZF<0) IP=data[IP+1]; break;
                case JLE: if(ZF<=0) IP=data[IP+1]; break;
                case JMP: IP=data[IP+1]; break;

                case PUSH: s.push(data[IP+1]); IP+=2 ; break;
                case PUSH_AX: s.push(AX); IP++ ; break;
                case PUSH_BX: s.push(BX); IP++ ; break;
                case POP: AX=s.pop(); IP++; break;

                case CALL:  IP++; cs.push(IP); IP=data[IP]; break;
                case RET: IP=cs.pop(); IP++; break;

                default: error(); SB=true;
            }
        }
    }

    private void interrupt()
    {
        IP++;

        switch(data[IP])
        {
            case PRINT: print(); break;
            case PRINTSHORT: System.out.print(AX); break;
            case PAUSE: break; //System.console().readLine(); break;
            case STOP: SB=true; break;
            default: error(); SB=true;
        }
    }

    private void print()
    {
        char tmp;
        short i=BX;

        while(true)
        {
            tmp=(char)(data[i]%256);
            if(tmp=='\0') break;
            System.out.print(tmp);
            i++;
            if(i>10000)
            {
                SB=true;
                return;
            }
        }
    }

    private void error()
    {
        char[] out=new char[4];

        System.out.print("\nError in ");

        out[0]=(char)(IP/1000%10+'0');
        out[1]=(char)(IP/100%10+'0');
        out[2]=(char)(IP/10%10+'0');
        out[3]=(char)(IP%10+'0');

        System.out.print(out);
    }

    public void init_test()
    {
        data[100]=PUSH;
        data[101]=308;
        data[102]=CALL;
        data[103]=1000;

        data[104]=PUSH;
        data[105]=2;
        data[106]=PUSH;
        data[107]=2;
        data[108]=CALL;
        data[109]=2000;

        data[110]=POP;
        data[111]=INT;
        data[112]=PRINTSHORT;

        data[113]=INT;
        data[114]=PAUSE;
        data[115]=INT;
        data[116]=STOP;
//-----------функция печати(воид без параметров)---------
        data[308]='A';
        data[309]='l';
        data[310]='e';
        data[311]='x';
        data[312]='e';
        data[313]='y';
        data[314]='\0';

        data[1000]=POP;
        data[1001]=MOV_BX_AX;
        data[1002]=INT;
        data[1003]=PRINT;
        data[1004]=RET;
//----------функция а+б=в----------------
        data[1994]='\n';
        data[1995]='\0';
        data[1996]='+';
        data[1997]='\0';
        data[1998]='=';
        data[1999]='\0';

        data[2000]=POP;
        data[2001]=MOV_MEM_AX;
        data[2002]=2002;
        data[2003]=POP;
        data[2004]=MOV_MEM_AX;
        data[2005]=2005;

        data[2006]=MOV_BX;
        data[2007]=1994;
        data[2008]=INT;
        data[2009]=PRINT;

        data[2010]=MOV_BX;
        data[2011]=2002;
        data[2012]=MOV_AX_BX;
        data[2013]=INT;
        data[2014]=PRINTSHORT;

        data[2015]=MOV_BX;
        data[2016]=1996;
        data[2017]=INT;
        data[2018]=PRINT;

        data[2019]=MOV_BX;
        data[2020]=2005;
        data[2021]=MOV_AX_BX;
        data[2022]=INT;
        data[2023]=PRINTSHORT;

        data[2024]=MOV_BX;
        data[2025]=1998;
        data[2026]=INT;
        data[2027]=PRINT;

        data[2028]=MOV_BX;
        data[2029]=2002;
        data[2030]=MOV_AX_BX;
        data[2031]=MOV_BX;
        data[2032]=2005;
        data[2033]=MOV_MEM_BX;
        data[2034]=2034;
        data[2035]=MOV_MEM_MEM;
        data[2036]=2039;
        data[2037]=2034;
        data[2038]=ADD;
        data[2039]=2;
        data[2040]=PUSH_AX;
        data[2041]=RET;
    }
}
