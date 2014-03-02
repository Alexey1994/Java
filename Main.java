import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;
import java.util.Scanner;

public class Main
{
    private static short[] read_program(String name) throws FileNotFoundException, IOException
    {
        short data[]=new short[10000];
        byte buf[]=new byte[5];
        FileInputStream f = new FileInputStream(name);

        for(int i=0; i<10000; i++)
        {
            f.read(buf);
            data[i]+=(short)((buf[0]-'0')*1000);
            data[i]+=(short)((buf[1]-'0')*100);
            data[i]+=(short)((buf[2]-'0')*10);
            data[i]+=(short)(buf[3]-'0');
        }

        f.close();
        return data;
    }

    private static void write_program(String name, short[] buf) throws FileNotFoundException, IOException
    {
        FileOutputStream f = new FileOutputStream(name);
        byte tmp[]=new byte[5];

        for(int i=0; i<10000; i++)
        {
            tmp[4]='\n';
            tmp[3]=(byte)(buf[i]%10+'0');
            tmp[2]=(byte)(buf[i]/10%10+'0');
            tmp[1]=(byte)(buf[i]/100%10+'0');
            tmp[0]=(byte)(buf[i]/1000%10+'0');
            f.write(tmp);
        }

        f.close();
    }

	public static void main (String args []) throws IOException
	{
	    Intel8086 prc=new Intel8086();;
	    Scanner s=new Scanner(System.in);
	    short key;

        while(true)
        {
            System.out.println("1 - vipolnitj");
            System.out.println("2 - zagruzitj");
            System.out.println("3 - sohranitj");
            System.out.println("4 - zagruzitj testovuju programmu");
            System.out.println("5 - vihod");

            key=s.nextShort();

            switch(key)
            {
                case 1: prc.run(); break;
                case 2: prc=new Intel8086(read_program("a.txt")); System.out.println("zagruzhen"); break;
                case 3: write_program("a.txt", prc.get_data()); System.out.println("sohranen"); break;
                case 4: prc.init_test(); break;
                case 5: return;
            }

            System.out.println("");
        }
	}
}
