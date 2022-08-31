import java.util.Scanner;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;
import java.util.Map;
import java.util.HashMap;



class MemberIDPW               // --(회원가입/로그인만을 위한 클래스)
{
   private String pName;         // 이름
   private String pNum;         // 주민등록번호
   private String pid;            // 탑승객 id
   private String ppw;            // 탑승객 pw

   public MemberIDPW(String pid, String ppw, String pName, String pNum)
   {
      this.pid = pid;
      this.ppw = ppw;
     this.pName = pName;
     this.pNum = pNum;
   }

   public String getpid(){ return pid; }
   public String getppw(){ return ppw; }
   public String getpName(){ return pName; }
   public String getpNum(){ return pNum; }
   public void setpid(String pid){ this.pid = pid; }
   public void setpwd(String ppw){ this.ppw = ppw; }
   public void setpName(String pName){ this.pName = pName; }
   public void setpNum(String pNum){ this.pNum = pNum; }
}



class Record               //좌석팀 속성클래스
{   
     int [][] seats = new int[10][4]; 
    boolean isRun= true; // 반복 flag
       String strColumn; // 행 이름
      char inputColumn;
       int rowNum; // 열 번호
}

class TicketBooking            // 항공권 예매 클래스
{
   pHandler handler = new pHandler(100);    //회원가입/로그인 할때 사용하는 클래스 선언
   RandomMil rm = new RandomMil();          //신규가입시 마일리지 랜덤으로 지급
   String mYN, mYN1;                        //Scanner 사용할때 사용하는 변수
   String id,pw,name,num;                   //회원가입시 id,pw,name,num을 받기 위한 변수
   int mil;                                 //마일리지 변수(회원가입시 1000마일리지 추가) 
   char newCu = 'X';                        //쿠폰 변수(회원가입시 'O'로 변경)
   private int mi;                        //handler 클래스 안에 idx변수를 꺼내기 위한 변수
   private int idx;

   int getmi()                              // mi를 실행문에서 꺼내기 위한 클래스
   {
     mi = handler.getmemNum();
      return mi;
   }
   int getidx()
   {
      idx = handler.getidx();
      return idx;
   }

   void memberCheck()                        //회원가입 클래스
   {
      Scanner sc = new Scanner(System.in);
      System.out.print("계정이 있으십니까?(Y/N) : ");
      mYN = sc.next();
      String aorb;
      boolean aorb1 = true;
      
      if (mYN.equals("Y") || mYN.equals("y"))      
      {
         aorb = handler.memberLogin();
         id = handler.uid;
         pw = handler.pwd;
         do
         {
          if (aorb.equals("b"))
          {
             System.out.print("계정이 있으십니까?(Y/N) : ");
             mYN1 = sc.next();

             if (mYN1.equals("Y") || mYN1.equals("y"))
             {
               aorb = handler.memberLogin();
            id = handler.uid;
            pw = handler.pwd;
               continue;
             }
             else if (mYN1.equals("N") || mYN1.equals("n") )
             {
               aorb= "b";
               aorb1= false;
               break;
             }
           }
          else
          {
            aorb= "a";
            aorb1= true;
            break;
          }
         }
         while (aorb.equals("b") );
      }
      else
      {
         System.out.println("회원만 이용 가능한 서비스입니다.");
         System.out.println("(신규회원가입시 쿠폰+랜덤 마일리지 지급)");
            handler.MemberInsert();
          id = handler.uid;
          pw = handler.pwd;
          name = handler.name;
          mil = rm.ranMil(); 
          newCu = 'O';
         
      }

   }
}

class RandomMil
{
   int ranMil()
   {
      Random rand = new Random();
      int ranMil = rand.nextInt(20001)+10000;
      return ranMil;
   }
}



class pHandler   // 로그인 / 회원가입
{
   private MemberIDPW[] members;
   private int idx;
   private int nidx;
   private int memNum;
   String uid, pwd, name, num;
   String PNAn;

   int getmemNum()
   {
      return memNum;
   }

   int getidx()
   {
      return idx;
   }

   public pHandler(int num)
   {
      members = new MemberIDPW[num];                  //회원 배열 클래스 생성            --※resmember랑 다름!!
      idx = 0;
      nidx = 0;
   }

   public void MemberInsert()  //회원가입
   { 
      Scanner sc = new Scanner(System.in);

     do
     {
        System.out.print("아이디 : "); 
        uid = sc.next();
        if (!isUniqueID(uid))
            System.out.println("이미 사용중인 아이디 입니다. \n");
     }
      while (!isUniqueID(uid)); // 중복ID
     

      System.out.print("비밀번호 : "); 
      pwd = sc.next();

      System.out.print("이름 : "); 
      name = sc.next();
      
      do
      {
         System.out.print("주민등록번호(13자리('-'제외)) : ");
         num = sc.next();
         PNAn = isPNA(num);
      }
      while (PNAn.equals("Error"));
      

      members[idx] = new MemberIDPW(uid, pwd, name, num);
      memNum=idx;
      System.out.println("가입 완료!! \n");
      idx++; // 멤버 수
   }
  
   private boolean isUniqueID(String uid) // 아이디의 중복 여부
   { 
      if (idx == 0) 
         return true;

      for (int i = 0 ; i < idx ; i ++)
      {
         if (members[i].getpid().equals(uid))
            return false;
      }
      return true;
   }

  public String memberLogin()
   {
      Scanner sc = new Scanner(System.in);
      System.out.print("아이디 : "); 
      String uid = sc.next();
      String mblg = "a";

      System.out.print("패스워드 : "); 
      String pwd = sc.next();

      String msg = "존재하지 않는 아이디 입니다.";
     int i=0;

      for (i = 0; i < idx ; i++)
      {
       
         if (members[i].getpid().equals(uid))
         {// members배열에 입력한 아이디가 있으면
            if (members[i].getppw().equals(pwd))
            {
            msg = "로그인 되었습니다.";
            System.out.println(msg);
            memNum=i;
            return mblg = "a";
            }
         else
         {
            msg = "암호가 잘못되었습니다.";
            System.out.println(msg);
            return mblg = "b";
         }
         }
      }
     
      System.out.println(msg);
     
      return mblg = "b";
     //return mblg = "a";
   }

   
   
   static String isPNA(String pnum)   //주민등록번호 유효성검사
   {
      Scanner sc = new Scanner(System.in);
      int resultPN=0;
      int[] arr1;
      int[] arrS;
      int sum=0;
     String result = pnum;
      do
      {
         int[] arr = {2, 3, 4, 5, 6, 7, 8, 9, 2, 3, 4, 5};
         arr1 = new int[14];
         arrS = new int[13];

         for (int i=0;i<13 ;i++ )
            arr1[i] = Integer.parseInt(result.substring(i,i+1));
         for (int i=0;i<12 ;i++ )
            arrS[i] = arr[i] * arr1[i];
         for (int i= 0;i<12 ;i++ )
            sum +=arrS[i];
          
         resultPN  = 11-(sum%11);

         System.out.println();
          
         if (resultPN==10)
            resultPN = 0;

         if (arr1[12]==resultPN)
         {
            System.out.println("유효한 주민등록번호입니다.");
            return result;
         }
          else
         {
            System.out.println("잘못된 주민등록번호입니다.");
            return "Error";
         }
       }
       while (arr1[12]!=resultPN && result.length() !=13);
   }
}

class AirPlanePay                  //기내식 / 수화물을 결제할 것인지 물어보는 클래스
{
   static final int airFood =10000;   //기내식 
   static final int luggage =20000;   //수화물 20Kg이상이면 결제
   String destination;
   String YN;                                    //스캐너 변수
   boolean airFoodCheck = false;               //기본값 false, 아래 스캐너에서 y를 입력하면 true
   boolean luggageCheck = false;               //기본값 false, 아래 스캐너에서 y를 입력하면 true

   void pay(String sa)
   {
      Scanner sc = new Scanner(System.in);
      destination = sa;
      System.out.printf("[기내식/수화물]%n");         // 수정해야 함-- sPoint()응용해서 메소드를 만들어야 함
      System.out.println("기내식 가격 : "+airFood+"원");
      System.out.println("수화물 가격 : "+luggage+"원");
      
      //=============================(좌석 불러오기(ex)a1, b1,)=====================================
      System.out.println();
      //System.out.printf("%s - 기내식을 선택하시겠습니까? (Y/N) : ");
      System.out.print("기내식을 선택하시겠습니까?(Y/N) : ");
      YN = sc.next();
      if (YN.equals("Y") || YN.equals("y"))
         airFoodCheck = true;
     else
        airFoodCheck = false;
      
      //System.out.printf("%s - 수화물이 20KG 이상이십니까? (Y/N) : ");
      System.out.print("수화물이 20KG 이상이십니까?(Y/N) : ");
      YN = sc.next();
      if (YN.equals("Y") || YN.equals("y"))
         luggageCheck = true;
     else
       luggageCheck = false;

   }
   

   boolean getAFC()
   {
      return airFoodCheck;
   }
   boolean getLGC()
   {
      return luggageCheck;
   }

}


class SeatSet extends Record 
{
   int a, b;
   String c;
   int cancel = 1;
   
   int getcancel()
   {
      return cancel;
   }
   int getb()
   {
      return b;
   }

   String getc()
   {
      String d=Achg(a);
      c = ""+d+b;
      return c;
   }
   
   String Achg(int i)
   {
      if (i == 0)
         return "A";
      else if (i == 1)
         return "B";
      else if (i == 2)
         return "C";
      else if (i == 3)
         return "D";
      else if (i == 4)
         return "E";
      else if (i == 5)
         return "F";
      else if (i == 6)
         return "G";
      else if (i == 7)
         return "H";
      else if (i == 8)
         return "I";
      else if (i == 9)
         return "J";
      else
         return "X";
   }

   void SetFBE(int y,int sy)
   {
      seats[y][sy-1] = 1;

   }

      void SeatImpl()   //-- 좌석 조회
      {
         System.out.println();
         System.out.println("───────────────────────────────────────────");
         System.out.println();
         System.out.print(" [ "+"행/열"+ " ] ");
         
         for (int i = 0; i < 1; i++) 
            {
               for (int j = 0; j < seats[i].length; j++) 
                  {
                     System.out.print(" ");
                     System.out.print(" [ "+"0"+(j+1)+" ]   ");
                  }
      
            } 
            System.out.println();
         
         for (int i = 0; i < seats.length; i++) 
            {
                System.out.print(" [   "+ (char)(i+65) +"   ] ");      
                System.out.print("  ");

                for (int j = 0; j < seats[i].length; j++) 
                  {   
                      if(seats[i][j] == 0) 
                         System.out.print("[ □ ]     ");
                     else 
                         System.out.print("[ ■ ]     ");
                  }
               System.out.println();
            
            }
         System.out.print("───────────────────────────────────────────");
         System.out.println();            
      }

      

      void  print(String seatclass)//-- 좌석 예약
      {
         Scanner sc = new Scanner(System.in);
         do
         {
            System.out.print("\n예약하실 행 이름을 입력해주세요  : ");

            strColumn = sc.next().toUpperCase();
            
            inputColumn = strColumn.trim().charAt(0);

            System.out.println("입력한 행 : " +inputColumn);

            if(inputColumn < 65 || inputColumn > 74) 
               {
                   System.out.println("선택할 수 없는 좌석입니다");
                   continue;
               } 

            int column = inputColumn - 65;

            System.out.print("예약하실 좌석의 열 번호를 입력해주세요 : ");
            
            rowNum = sc.nextInt();

            if(rowNum < 1 || rowNum > 4) 
               {
                    System.out.println("선택할 수 없는 열 번호입니다");continue;
               }

         System.out.println("선택하신 좌석은 : " +inputColumn+ " 행이고 " + rowNum + " 열입니다");
         System.out.print("예약 완료 하시겠습니까 ? (Y / N) : ");
         String s = sc.next();
         a = column;
         b = rowNum;

          if((seats[column][rowNum-1]== 0))
         {
            if(s.equals("y") || s.equals("Y")) 
            {
               seats[column][rowNum-1] = 1;
               System.out.println("예약이 완료되었습니다");
               isRun=false;
               cancel = 1;
            }   
            else       
               System.out.println("취소되었습니다");
               cancel = 0;
         }
         else
            System.out.println("이미 예약된 좌석입니다. 다른 좌석을 선택해주세요.");
         seats[column][rowNum-1] = 0;
      } 
      while (isRun);
      }

}

class PayCalCulate                              //계산 및 영수증 출력
{
   char food = 'X';
   char lug = 'X';
   String seatClass;
   String seatName;
   int seatPay;
   
   void calPrint(String sClass, String sName, boolean acheck, boolean lcheck, int year, int month, int day, String time, char coupon)      //계산기 프린트
   {
      seatClass = sClass;
      seatName = sName;
      if (acheck == true)
         food = 'O';
      if (lcheck == true)
         lug = 'O';

      
      
      System.out.println();
      System.out.println("==========결제 영수증==========");
      System.out.printf("%d/%d/%d %s출발 %n",year,month,day,time);       //※ 수정해야함.  날짜팀 클래스 사용해서 출력
      System.out.printf("분류 : %s%n",seatClass);               //분류 :이코노미, 퍼스트, 비즈니스
      System.out.printf("좌석 : %s%n",seatName);            //좌석 :A1, B1, C1
      System.out.printf("기내식 : %c%n",food);               // 기내식 : O, X
      System.out.printf("수화물 20KG 이상 : %c%n",lug);         // 수화물 : O, X
      System.out.printf(" 할인쿠폰(10%%) : %c%n",coupon);         // 할인쿠폰 O      일단 만들어 놓음
      System.out.println("");
   }

   void TotalCal (String seat)
   {
      if (seat.equals("퍼스트"))
      seatPay = 200000;
      else if (seat.equals("비즈니스"))
      seatPay = 100000;
      else
      seatPay = 50000;
      
   }
}

class SeatCheck                        //좌석 선택 클래스
{
   int Check()
   {
      int n = 4;
      Scanner sc = new Scanner(System.in);
      System.out.println("좌석등급을 선택해주세요.");
      System.out.println("1. 퍼스트 클래스 ");
      System.out.println("2. 비즈니스 클래스 ");
      System.out.println("3. 이코노미 클래스 ");
      System.out.println("4. 종료 ");

      System.out.print("항목 번호를 입력해주세요 : ");
        n = sc.nextInt();

      return n;
   }
}

class Ticket                  //티켓 제작, 출력 클래스
{
   String ticketNum;          // 티켓번호
   String point;              // 행선지
   String name;               // 이름
   String seat;               // 좌석번호
   String sclass;             // 좌석등급
   int peoCount=1;            // 몇명인지
   int roTrip=1;              // 왕복
   int tyear,tmonth,tday;
   String ttime;

   void YMDSetting(int year, int month, int day,String time)
   {
      tyear = year;
      tmonth = month;
      tday = day;
      ttime = time;
   }


   void TicketPrint(String po,String pe,String se,String sc)      //출력메소드
   {   
      point = po;
      name = pe;
      seat = se;
      sclass = sc;

      char gate = gateMaking(point);
      String start = startMaking(point);
      String end = endMaking(point);

      //for (int i=0; i<roTrip*peoCount; i++)            //왕복이면 한번더
      //{
      ticketNum = ""+tyear+tmonth+tday + gate + point + ttime + seat;

      System.out.println("===========================================");
      System.out.printf("\t     [%s 클래스]%n",sclass);
      System.out.printf(" 예약자 : %s   \t    탑승구 : %c%n",name,gate);
      System.out.printf("   \t\t\t    좌석 : %s",seat);
      System.out.println();
      System.out.printf("\t      %s → %s%n", start,end);
      System.out.printf("\t%d/%d/%d %s 출발 비행기%n",tyear,tmonth,tday,ttime); // 날짜팀 받아서 출력
      System.out.printf(" \t항공권번호 : %s%n",ticketNum);
      System.out.println("===========================================");
      //}
   }

   char gateMaking(String point)      //탑승구 제작 메소드
   {
      if (point.equals("SP") || point.equals("PS"))
         return 'A';
      else if (point.equals("SB") || point.equals("BS"))
         return 'B';
      else if (point.equals("SJ") || point.equals("JS"))
         return 'C';
      else 
         return 'D';
   }

   String startMaking(String point)   //영어대문자를 출발지로 다시 바꿔주는 메소드
   {
      char result = point.charAt(0);
      if (result == 'S')
         return "서울";
      else if (result == 'P')
         return "부산";
      else if (result == 'J')
         return "제주";
      else
         return "에러";
   }
   String endMaking(String point)      //영어대문자를 출발지로 다시 바꿔주는 메소드
   {
      char result = point.charAt(1);
      if (result == 'S')
         return "서울";
      else if (result == 'P')
         return "부산";
      else if (result == 'J')
         return "제주";
      else
         return "에러";
   }

   String getTicketNum()
   {
      return ticketNum;
   }
}

class resMember            //★--예약 회원 배열 활용해서 번호에 따라 티켓이나 클래스 속 변수가 달라지게 만들어야함.
{
   String ID="";   
   String PW="";
   String point="";
   String rName="";   
   char coupon='O';
   int mil=0;   //마일리지
   int count=0;
   private int hasT;            // 인원수, 티켓보유여부
   String rSeat="";
   String rClass="";      //좌석이름, 좌석등급
   String time;
   int membernumber;    //부여된 번호  → 이걸로 예약을 했었는지 확인 가능
   int year,month,day;
   
   
   
   void setYMDT(int y,int m,int d,String t)
   {
      year = y;
      month = m;
      day = d;
      time = t;
   }

   void setMember(String id, String pw, int mil, String point, String name, String seatName, int n, String c, int num)
   {
      this.ID = id;
      this.PW = pw;
      this.mil = mil;
      this.point = point;
      rName = name;
      rSeat = seatName;
      this.hasT = n;
      rClass = c;
      membernumber = num;
   }

   String getRClass()
   {
      return rClass;
   }
   int gethasT()
   {
      return hasT;
   }
    void sethasT(int t)
   {
       hasT = t;
   }


}



class ChangeMonth{
   Scanner sc = new Scanner(System.in);
      
   int input()
   {

      int selectNum0;

      System.out.println("1. 날짜 선택");
      System.out.println("2. 이전 달 보기");
      System.out.println("3. 다음 달 보기");         
      do
      {
         selectNum0 = sc.nextInt();      
      }
      while(selectNum0>3 || selectNum0<1);

      return selectNum0;

   }
}

class CalenderPrint //년월 입력, 캘린더 그려주기
	
	{	

		Scanner sc = new Scanner(System.in);
		Calendar cal = new GregorianCalendar();
		
		int y;
		int m;
		int w;

		int nowy = cal.get(Calendar.YEAR);		
		int nowm = cal.get(Calendar.MONTH)+1;	
		int nowd = cal.get(Calendar.DATE);
		

		int inputYear(){
	

		do
		{
			System.out.print("연도 입력 : ");
			y = sc.nextInt();
		}
		while (y < 1 || y<nowy);

		return y;
		
		}

		int inputMonth(){
		do
		{
			System.out.print("월   입력 : ");
			m = sc.nextInt();
		}
		while (1 > m || m > 12 || (y==nowy && m<nowm));

		return m;
		}

		void print(int year, int month){
		int w;		
		int i;

		
		cal.set(year, month-1, 1);
		w = cal.get(Calendar.DAY_OF_WEEK);
		
		System.out.println();
		System.out.println("\t[ " + year + "년 " + month + "월 ]\n");	
		System.out.println("  일  월  화  수  목  금  토");
		System.out.println("============================");
		for (i=1; i<w; i++)
		{
			System.out.print("    ");		// 공백 4칸 넣어줌
		}

		for (i=1; i<=cal.getActualMaximum(Calendar.DATE); i++)		
			{
			System.out.printf("%4d", i);
			w++;				
			if (w%7==1)
			{
				System.out.println();
				}
		}
		
		if (w%7!=1)						
			{
					System.out.println();	

		}
		System.out.println("============================");
		}


}

class DaySetting extends CalenderPrint //일 입력하는 클래스

{
      CalenderPrint CP = new CalenderPrint();
      ChangeMonth CM = new ChangeMonth();
	  int nyear;
	  int nmonth;


      Scanner sc = new Scanner(System.in);
      int year;
      int month;
      int day;
      int nowYear;
      int nowMonth;
      int nowDay;
	  
      
      boolean breakAlp = true;

      void getNowYear(int ny)
		{//현재 년 불러오는 메소드
         nowYear = ny;

      }
      void getNowMonth(int nm){//현재 월 불러오는 메소드
         nowMonth = nm;   
      }
      void getNowDay(int nd){//현재 일 불러오는 메소드
         nowDay = nd;
      }

      void getYear(int gy){//예매할 년도 불러오는 메소드
         year = gy;
      }

      void getMonth(int gm){//예매할 달 불러오는 메소드
         month = gm;
      }


      int input(int selectNum)
      {
		breakAlp = true;

         //아무래도 이곳에서 무한루프 오류가 나는 것 같습니다..ㅠㅠ
         while(breakAlp == true)
         //while(true)

         {
            switch(selectNum)
            {
               case 1 :
                  System.out.print("날짜 입력 ! ");
                  day= sc.nextInt();
                  
                  if(year == nowYear && month == nowMonth && nowDay>=day)
                  {
                     do
                     {
                        CP.print(year,month);
                         System.out.printf("오늘 이후 날짜 입력 (오늘 날짜 = %d년 %d월 %d일 ): ",nowYear,nowMonth,nowDay);         
                         day= sc.nextInt();
                     
                     }
                     while(day<=nowDay);
                     breakAlp = false;
                     break;   
                  }
                  else
                  {
                     breakAlp = false;
                     //return day;
                     break;                  
                  }

                     
                 case 2 : 
                  if( nowYear==year && nowMonth == month)
                  {
                     int selectNum1;
                     CP.print(year, month);   
                     System.out.println("현재달 이전달은 선택이 불가합니다. 다른 선택지를 호출하세요.");

                     selectNum = CM.input(); //메뉴 1234중 선택한값 넘겨줌
                     breakAlp = true;
                     //day = DS.input(selectNum1);
                     break;
                  }
                  else if( nowYear==year )
                  {
                     
                     month -= 1;
                     if (month <= 0)
                     {
                        month = 12;
                     }
                     
                     CP.print(year, month);
                     //monthfix = month-1;


                     selectNum = CM.input(); //메뉴 1234중 선택한값 넘겨줌
                     breakAlp = true;
                     
                     break;

                  }
                  else if (nowYear != year)
                  {
                     month -= 1;
                     if (month <= 0)
                     {
                        month = 12;
                        year = year-1;
                     }
 
                     CP.print(year, month);

                     selectNum = CM.input(); //메뉴 1234중 선택한값 넘겨줌
                     breakAlp = true;
               
                     break;

                  }


               case 3 : month = month + 1;
                  if(month>12)
                  {
                     month = 1;
                     year +=1;
                  }
                  CP.print(year, month);
                  selectNum = 1;

                  //monthfix = month-1;
                  //yearfix = year-1;
                  

                  selectNum = CM.input(); //메뉴 1234중 선택한값 넘겨줌
                  breakAlp = true;
                  //return day;
                  break;

            
            
            }
            
            
         }

class CalenderPrint //년월 입력, 캘린더 그려주기
	
	{	

		Scanner sc = new Scanner(System.in);
		Calendar cal = new GregorianCalendar();
		
		int y;
		int m;
		int w;

		int nowy = cal.get(Calendar.YEAR);		
		int nowm = cal.get(Calendar.MONTH)+1;	
		int nowd = cal.get(Calendar.DATE);
		

		int inputYear(){
	

		do
		{
			System.out.print("연도 입력 : ");
			y = sc.nextInt();
		}
		while (y < 1 || y<nowy);

		return y;
		
		}

		int inputMonth(){
		do
		{
			System.out.print("월   입력 : ");
			m = sc.nextInt();
		}
		while (1 > m || m > 12 || (y==nowy && m<nowm));

		return m;
		}

		void print(int year, int month){
		int w;		
		int i;

		
		cal.set(year, month-1, 1);
		w = cal.get(Calendar.DAY_OF_WEEK);
		
		System.out.println();
		System.out.println("\t[ " + year + "년 " + month + "월 ]\n");	
		System.out.println("  일  월  화  수  목  금  토");
		System.out.println("============================");
		for (i=1; i<w; i++)
		{
			System.out.print("    ");		// 공백 4칸 넣어줌
		}

		for (i=1; i<=cal.getActualMaximum(Calendar.DATE); i++)		
			{
			System.out.printf("%4d", i);
			w++;				
			if (w%7==1)
			{
				System.out.println();
				}
		}
		
		if (w%7!=1)						
			{
					System.out.println();	

		}
		System.out.println("============================");
		}


}

		nyear=year;
		nmonth=month;
        return day;
      }



}



class TimeTable
{	
	// 이용자가 입력한 「연, 월, 일, 출발지, 도착지」 를 매개변수로 받아
	// 항공편을 출력하고
	// 이용자가 선택한 항공편의 출발 시간을 리턴해주는 메소드
	String timetable(int year, int month, int day, String depart)	
	{
		Scanner sc = new Scanner(System.in);

		String dep;
		String time;
		int num;
	
		String[] table = {"\t\t①    06 : 30 ~ 07 : 25", "\t\t②    08 : 30 ~ 10 : 25", "\t\t③    10 : 30 ~ 12 : 25", "\t\t④    12 : 30 ~ 14 : 25", 
							"\t\t⑤    14 : 30 ~ 17 : 25", "\t\t⑥    16 : 30 ~ 18 : 25", "\t\t⑦    18 : 30 ~ 20 : 25", "\t\t⑧    20 : 30 ~ 21 : 25", 
							"\t\t⑨    22 : 30 ~ 23 : 25"};
		
		int k = 0;

		if (depart  == "SP")	// 출발지 : 서울 → 도착지 : 부산
		{
			k = 0;
		}
		else if (depart== "SJ")	// 출발지 : 서울 → 도착지 : 제주
		{
			k = 1;
		}
		else if (depart == "PS")	// 출발지 : 서울 → 도착지 : 제주
		{
			k = 2;
		}
		else if (depart == "PJ")	// 출발지 : 서울 → 도착지 : 제주
		{
			k = 3;
		}
		else if (depart  == "JS")	// 출발지 : 서울 → 도착지 : 제주
		{
			k = 4;
		}
		else if (depart  == "JP")	// 출발지 : 서울 → 도착지 : 제주
		{
			k = 5;
		}

		switch (depart)
		{
			case "SJ" : dep = "서울 -> 제주"; break;
			case "PS" : dep = "부산 -> 서울"; break;
			case "PJ" : dep = "부산 -> 제주"; break;
			case "JS" : dep = "제주 -> 서울"; break;
			case "JP" : dep = "제주 -> 부산"; break;
			case "SP" : dep = "서울 -> 부산"; break;
			default : dep = ""; break;
		}

		System.out.printf("\n■■■■■ %d년 %d월 %d일 %2s 항공편입니다 . ■■■■■\n ", year, month, day, dep);
		System.out.println();
		System.out.println("\t※ 오전 및 심야 시간 10% 할인 적용 (얼리버드 제외) ※");
		System.out.println();

		for (int i=0; i<9; i++)
		{	
			System.out.println(table[i]);
			System.out.println();
		}
		System.out.println();

		do
		{		
			System.out.print(" ■ 원하는 항공편의 번호를 선택해 주세요 : ");	
			num = sc.nextInt();
		}
		while (num<1 || num>9);

		switch (num)
		{
		case 1 : time = "0630"; break;
		case 2 : time = "0830"; break;
		case 3 : time = "1030"; break;
		case 4 : time = "1230"; break;
		case 5 : time = "1430"; break;
		case 6 : time = "1630"; break;
		case 7 : time = "1830"; break;
		case 8 : time = "2030"; break;
		case 9 : time = "2230"; break;
		default : time = ""; break;
		}

		return time;
	}
}
class pPoint                     //출발지 도착지 클래스
{
   private String start;
   private String end;
   String result;

   void Pointinput()               //입력 클래스
   {
      Scanner sc = new Scanner(System.in);
      do
      {
      System.out.println("출발지, 도착지를 선택해주세요");
      System.out.print("서울/부산/제주(공백 구분) : ");
      start = sc.next();
      end = sc.next();
      }
      while (start.equals(end) || (!start.equals("서울") && !start.equals("부산") && !start.equals("제주")) 
        || (!end.equals("서울") && !end.equals("부산") && !end.equals("제주")));

      result = sPoint();
   }
   
   String sPoint()        //출발지 도착지 변환 클래스
   {

      Scanner sc = new Scanner(System.in);
      String result;
      String result1="";
      String result2="";

      if (start.equals("서울"))
         result1= "S";
      else if(start.equals("부산"))
         result1= "P";
      else if(start.equals("제주"))
         result1= "J";
      else
         result1= "E";
      
      if (end.equals("서울"))
         result2= "S";
      else if(end.equals("부산"))
         result2= "P";
      else if(end.equals("제주"))
         result2= "J";
      else 
         result2= "E";
      result = result1 + result2;
      return result;
   }
}


class setMoney
{
int deMoney (String depart)
{
   int priceDepart = 1000;
   
   
   if(depart.equals("SJ"))
   {
      priceDepart = 120000;
   }
   else if(depart.equals("JS"))
   {
      priceDepart = 120000;
   }
   else if(depart.equals("SP"))
   {
      priceDepart = 100000;
   }
   else if (depart.equals("PS"))
   {
      priceDepart = 100000;
   }
   else if (depart.equals("PJ"))
   {
      priceDepart = 60000;
   }
   else if (depart.equals("JP"))
   {
      priceDepart = 60000;
   }

      return priceDepart;
}

int levelMoney(String seatclass, int price)
{
   int priceLevel = price;

   if(seatclass.equals("이코노미")){
      priceLevel = priceLevel;
   }
   else if(seatclass.equals("비지니스")){
      priceLevel = priceLevel * 2;
   }
   else if(seatclass.equals("퍼스트")){
      priceLevel = priceLevel * 3;
   }
   
   return priceLevel;
}

}

class Discount
{

  // 가용한 할인을 모두 적용하여 최종 결제 가격을 return 해주는 메소드
  int discount(int money, boolean food, boolean lug, char cou, int year, int month, int day, String time) 
  {
   int price;
   boolean discount = false;
   String dash = "-";

   double saleprice, calcprice;
   int fprice = 10000, lprice = 20000;         
   
   if (time.equals("0630") || time.equals("0830") || time.equals("1030") || time.equals("2230"))
   {
      discount = true;
   }
   else
      discount = false;
      
   int number1 = year;
   String str1 = Integer.toString(number1); 

   int number2 = month; 
   String str2 = Integer.toString(number2); 

   int number3 = day; 
   String str3 = Integer.toString(number3); 
      
   String date = str1 + dash + str2 + dash + str3;
      
   int calMonth = 0;
   int compare = 0;
   SimpleDateFormat df = new SimpleDateFormat ("yyyy-MM-dd");
   String today = df.format (System.currentTimeMillis());

   try
   {
      SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
      Date currentDate = format.parse(today);
      Date lastDate = format.parse(date);
      long calDate = currentDate.getTime() - lastDate.getTime();
      long calDateDays = (calDate / (24*60*60*1000))*-1;             // 현재 날짜로부터 선택 연월일까지의 일 수 차이 = calDateDays
      calMonth = (int)calDateDays-180;                        // calMonth >=0 : 얼리버드 시기 (6개월 이상) 
   }
   catch (ParseException e)
   {
   }
   
   if (calMonth<0)      // 얼리버드 이외의 시기 
   {
      if (discount)   // 오전심야시간대라면
      {
         saleprice = money*0.1;
         calcprice = money-saleprice;            
         money = Integer.parseInt(String.valueOf(Math.round(calcprice)));
      }
      else
         money *= 1;
   }
   else
      money *= 1;

   if (food == true )
   {
      money += fprice;
   }
   else
      money *= 1;

   if (lug == true)
   {
      money += lprice;
   }
   else
      money *= 1;
   
   if (cou == 'O')                     
   {
      saleprice = money*0.1;
      calcprice = money-saleprice;            
      price = Integer.parseInt(String.valueOf(Math.round(calcprice)));
   }
   else
      price = money;
   
   return price;
  }
}
class moneyCal
{
//여기 클래스부분 전체 바뀌었음.

int price = 0;
int getmileage; 
int mileage;
int useMileage(int priceM, int gmileage){ //좌석별/행선지별 금액 , 사용자가 가지고 있던 마일리지
   Scanner sc = new Scanner(System.in);
      
      char choiseM; //마일리지 사용여부 선택
      int usedMileage = 0; //사용한 마일리지 저장
      if(gmileage>0){
       System.out.printf("마일리지를 사용하시겠습니까?(Y/N) 금액(%d): ",priceM);
        choiseM = sc.next().charAt(0);
       if (choiseM == 'Y'|| choiseM == 'y')
         {
         do{
         System.out.printf("사용할 마일리지를 입력(잔여 마일리지 %d) : ",gmileage);
         usedMileage = sc.nextInt();
         }while(usedMileage > gmileage || usedMileage ==0); //잔여마일리지보다 사용할 마일리지가 많으면 반복

         mileage = gmileage - usedMileage; //보유한 마일리지에서 사용한 마일리지 빼줌
            
         price += priceM - usedMileage; // 원래 티켓값에서 사용한 마일리지값 빼줌.
         return usedMileage; //사용한 마일리지값 얼마인지 반환
      }
       else
         {
        price = priceM;
         return usedMileage;    
       }
      }

   
      return usedMileage;
}

int getMoney(int leftMileage)
{
      int money = 0;
      int inputm = 0;
      int submoney;
      int sum = 0;
      Scanner sc = new Scanner(System.in);

      
      do{
      System.out.printf("금액을 투입하세요 (금액 : %d): ",price);
      inputm = sc.nextInt();
      if(inputm<price)
         {
         sum += inputm;
         if(sum > price){
            return sum;
         }
         else if(sum>=price){
         sum = price;
         return sum;
         
         }
         }
      
      
      else if(inputm >= price)
      {   //거스름돈 반환
      sum = inputm;
      return inputm;
      }
      }while (sum<price);
   
      return sum;

}

   


void calMoney(int getMoney) 
   {   
      Scanner sc = new Scanner(System.in);
      int submoney;
      int mileselect;
      submoney = getMoney - price;
      if(getMoney == price)
      {
         System.out.printf("거스름돈이 없습니다.%n");
         System.out.printf("잔여 마일리지 : %d%n",mileage);
      }
      //
      else
      {

      System.out.printf("거스름돈: 1. 현금반환 2. 마일리지 적립 : ");
      mileselect = sc.nextInt(); 
      
      if(mileselect == 1){
      int[] coin = {50000,10000,5000,1000};
      int leftcoin = 999;
      
      for(int i = 0; i< coin.length; i++)
         {
         if((submoney/coin[i])>0)
            {
         System.out.println(coin[i] + "원: " + submoney/coin[i]);
         submoney %= coin[i];}
         if(leftcoin >= submoney){
            leftcoin = submoney; 
            getmileage = submoney;
            mileage += getmileage;
         }
               
         }
      
         System.out.printf("마일리지 적립 : %d%n",getmileage);
      }
      else if(mileselect == 2){
   
         mileage += submoney;
         System.out.printf("마일리지 적립 : %d%n",submoney);

                  }
         System.out.printf("잔여 마일리지 : %d",mileage);
         System.out.println();
      
      }
   }



//마일리지 저장하는 메소드
int mileageCal(int price, int getMoney, int mileage){
      
      int submoney;
      submoney = getMoney - price;
      
      int leftcoin = 999;
      

         if((submoney/1000)>0)
            {
         submoney %= 1000;}
         if(leftcoin >= submoney){
            leftcoin = submoney; 
            getmileage = submoney;
            mileage += getmileage;               
         }
      return mileage;
}

}




class Mapseat
{
   static String result;
   String sumResult;
   
   String outPut()
   {
   return result;
   }

   void sumMap(String s)
   {
      result += ""+s;
   }

   String setSum(int y,int m, int d, String time,String point, String pclass)
   {
      sumResult = ""+y+m+d+time+point+pclass;
      return sumResult;
   }
}


public class FinalTest
{
   public static void main(String []args)
   {
      //클래스, 변수 선언
    Scanner sc = new Scanner(System.in);
     Calendar cal = new GregorianCalendar();
    TicketBooking TB = new TicketBooking();
     AirPlanePay APP = new AirPlanePay(); 
     SeatCheck SC = new SeatCheck();
     PayCalCulate PCC = new PayCalCulate();
     resMember[] member = new resMember[100];               //최대인원 100명
     pPoint PPO = new pPoint();            // 수정
     Ticket Tic = new Ticket();
     CalenderPrint CP = new CalenderPrint();
     ChangeMonth CM = new ChangeMonth();
     DaySetting DS = new DaySetting();
     TimeTable TT = new TimeTable();
     Ticket Tc = new Ticket();
   //  CheckSeat Cks = new CheckSeat();
    setMoney sm = new setMoney();
    moneyCal mc = new moneyCal();
    Discount dis = new Discount();
   
   Mapseat ms = new Mapseat();
   Map<String, String> map = new HashMap<String, String>();
   String[] hasharr = new String[80];

   


      int i=0;          //미완성 변수, 배열 수 세기 용         // 0번째 멤버 가입 - 예약 - 다른아이디 로그인 - 0번째 멤버 로그인 - 예약 조회
      int mi=0;         //미완성 변수, 멤버찾기용
      //member[i] = new resMember(TB.id,TB.pw,TB.mil,PPO.result,TB.name,PCC.seatName);   //member의 i번째 배열클래스 생성.
                                                                  //생성자로 인수를 받아 i번째 클래스의 내부변수에 배치
                                                                  //첫 루프엔 값이 없으므로 아무것도 안들어감

    int year = cal.get(Calendar.YEAR);   //실행 시점의 연도   
    int month = cal.get(Calendar.MONTH)+1;  //실행 시점의 달
    int day;
    int selectNum1;
    int nowYear;
    int nowMonth;
     int nowDay;
     String time;
     int price;
     int money;
     int mileage;//임의값
     char coupon = 'X';
     int usedMileage; //사용한 마일리지
         

      String pPlace;
      String tChoice;            //switch문 선택변수

      while(true)               //자판기 무한반복문
      {
      SeatSet SS = new SeatSet();   //반복 될 때마다 초기화
        
        System.out.println("[항공기 발권기]");
        System.out.println("안녕하세요.");
        System.out.println("항공기 발권기입니다.");
        System.out.println();
        TB.memberCheck();         //회원가입/로그인 클래스
        mi = TB.getmi();          //handler 의 memNum 변수 호출         --몇번째 멤버인지 찾아주는 변수 (0번째 회원가입 - 0번째 멤버 id/pw 일치시키면                                                       // 1 - 1
        member[i] = new resMember();

         System.out.println("[진행하실 항목을 선택해 주세요]");

         System.out.println();
         System.out.println("1. 항공권 예약 ");
         System.out.println("2. 항공권 조회 ");
         System.out.println("3. 항공권 잔여좌석 확인 ");
         System.out.println("4. 종료 ");
         System.out.println();
         System.out.print("항목 번호 : ");
         int choice = sc.nextInt();
         
         switch(choice)            //입력값에 따라 예약/조회/잔여좌석 확인으로 넘어가기 위한 switch문
         {
            case 1 :
            //PPO.Pointinput();         //수정
            //달력, 날짜 선택           //-- 날짜팀 클래스
          CP.print(year, month);      //연월 받아서 달력출력

            nowYear = cal.get(Calendar.YEAR);       //현재의 년도
            nowMonth = cal.get(Calendar.MONTH)+1;         //현재의 월
            nowDay = CP.nowd;         //현재의 일
            DS.getNowYear(nowYear);     //현재 년 불러오는 메소드
            DS.getNowMonth(nowMonth);   //현재 월 불러오는 메소드
            DS.getNowDay(nowDay);      //현재 일 불러오는 메소드

            DS.getYear(year);         //예매할 년도 불러오는 메소드(사용자에게 입력받은 년도)
            DS.getMonth(month);         //예매할 달 불러오는 메소드(사용자에게 입력받은 달)

            selectNum1 = CM.input();   //메뉴 1234중 선택한값 넘겨줌
            day = DS.input(selectNum1); //1234중 선택한 메뉴 실행 후 입력받은 날짜 반환
         

            System.out.printf("%d년 %d월 %d일",DS.year,DS.month,day);


            PPO.Pointinput();         //수정  //날짜팀 제작은 아니지만 이 위치에 넣어야 해서 삽입함.
         time = TT.timetable(year,DS.month,day,PPO.result);
            
            //TT.timetable(DS.nyear, DS.nmonth, day, pPoint.result.charAt(0), pPoint.result.charAt(0));
            //String hh = TT.timetable(DS.year, DS.month, day, PPO.sPoint());
         //System.out.print(hh);

            //날짜팀 제작 클래스         
            //시간 선택                    //-- 날짜팀 클래스

         


           int c = SC.Check();            //좌석 어떤것 선택할것인지 묻는 클래스
            
         //값계산

         String seatclass="에러";
   
       switch (c)                     //SC.Check값에 따라 퍼스트, 비즈니스, 이코노미 선택
            {
            case 1:  seatclass="퍼스트"; break;
            case 2:  seatclass="비즈니스"; break;
            case 3:  seatclass="이코노미"; break;
         case 4: break;
            }
       
       String seatvalue="";            //값 초기화
         String YMDTPSC = ms.setSum(year,month,day,time,PPO.result,seatclass);
         if (map.get(YMDTPSC)!=null)
         {
            seatvalue = map.get(YMDTPSC);      //YMDTPSC에 따른 값 꺼내기(null처리)
         }
         
         
         int sv=0;
         try                        //예외처리구문
         {
            sv=seatvalue.length();
         }
         catch(NullPointerException e)
         {
            sv=0;
         }
                  
         int j=0;
         int a=0;
         int b=0;
         for (int ai=0; ai<=sv; ai +=2,j++)         //a1 b1 c2 d2 적재
         {
            if (ai==sv)
            {
            break;
            }
            String z = map.get(YMDTPSC);      //a1b1c2d2

            if (z.charAt(ai+1)-'0' !=0)
            {
               if (z.charAt(ai) == 'A')
               {  
                  a = 0;
                  b = z.charAt(ai+1) - '0';
               }
               else if(z.charAt(ai) == 'B')
               {
                  a = 1 ;
                  b = z.charAt(ai+1) - '0';
               }
               else if(z.charAt(ai) == 'C')
               { 
                  a = 2 ;
                  b = z.charAt(ai+1) - '0';
               }
               else if(z.charAt(ai) == 'D')
               {
                  a = 3 ;
                  b = z.charAt(ai+1) - '0';
               }
               else if(z.charAt(ai) == 'E')
               {
                  a = 4 ;
                  b = z.charAt(ai+1) - '0';
               }
               else if(z.charAt(ai) == 'F')
               {
                  a = 5 ;
                  b = z.charAt(ai+1) - '0';
               }
               else if(z.charAt(ai) == 'G')
               {
                  a = 6 ;
                  b = z.charAt(ai+1) - '0';
               }
               else if(z.charAt(ai) == 'H')
               {
                  a = 7 ;
                  b = z.charAt(ai+1) - '0';
               }
               else if(z.charAt(ai) == 'I')
               {
                  a = 8 ;
                  b = z.charAt(ai+1) - '0';
               }
               else if(z.charAt(ai) == 'J')
               {
                  a = 9 ;
                  b = z.charAt(ai+1) - '0';
               }
            SS.SetFBE(a,b);
            }
         }
      
      do
         {
            SS.SeatImpl();                  //좌석 보여주기
            SS.print(seatclass);            //좌석 입력하기
         }
         while (SS.getcancel() == 1);
         
         seatvalue += SS.getc();
         map.put(YMDTPSC,seatvalue);

         APP.pay(PPO.result);
         PCC.calPrint(seatclass,SS.getc(),APP.getAFC(),APP.getLGC(),year,month,day,time,coupon);
         YMDTPSC = ms.setSum(year,month,day,time,PPO.result,seatclass);


       member[mi].setMember(TB.id,TB.pw,TB.mil,PPO.result,TB.name,PCC.seatName,1,seatclass,mi);   //예약멤버 배열
       member[mi].setYMDT(year,DS.month,day,time);
       Tic.YMDSetting(member[mi].year,member[mi].month,member[mi].day,member[mi].time);

            
           //값계산
            price = sm.deMoney(member[mi].point);
            mileage = member[mi].mil;
            price = sm.levelMoney(member[mi].rClass, price);
            price = dis.discount(price, APP.airFoodCheck, APP.luggageCheck, coupon, year, month, day, time);
            usedMileage = mc.useMileage(price, mileage);   
            member[mi].mil = mileage - usedMileage;
            
            money = mc.getMoney(mileage);
            mc.calMoney(money);
            member[mi].mil = mc.mileageCal(price,money,member[mi].mil);

         coupon = member[mi].coupon;


            System.out.print("티켓을 출력하시겠습니까?(Y/N) :");
            tChoice = sc.next();                     

         if (tChoice.equals("Y") || tChoice.equals("y"))
            {
         Tic.YMDSetting(member[mi].year,member[mi].month,member[mi].day,member[mi].time);
            Tic.TicketPrint(member[mi].point,member[mi].rName,member[mi].rSeat,member[mi].rClass);      //티켓 출력
            }
            break;           

         case 2 :
            if (member[mi].gethasT() == 1)         //예약회원이 티켓을 가지고 있으면 티켓을 출력할 수 있게끔.(배열변수 활용해서 완성해야함)
            {
               System.out.print("예약된 티켓이 있습니다. 출력합니까?(Y/N) :");
               tChoice = sc.next();
               if (tChoice.equals("Y") || tChoice.equals("y"))
               {
            Tic.YMDSetting(member[mi].year,member[mi].month,member[mi].day,member[mi].time);
               Tic.TicketPrint(member[mi].point,member[mi].rName,member[mi].rSeat,member[mi].rClass); //출-도,예약자이름,예약좌석번호,예약좌석등급
               }
            System.out.print("항공권을 변경, 혹은 취소 하시겠습니까? (Y/N) : ");
            tChoice = sc.next();
            if (tChoice.equals("Y") || tChoice.equals("y"))
               {
            System.out.println("1. 항공권 변경");
            System.out.println("2. 항공권 취소");
            System.out.println("3. 종료");
            System.out.println();
            System.out.print("항목을 입력해주세요 : ");
            choice = sc.nextInt();
            String realYn;

            switch (choice)
            {
            
            case 1:
               System.out.print("정말로 예약을 변경하시겠습니까? (Y/N) : ");   
               realYn = sc.next();
               if (realYn.equals("Y") || realYn.equals("y"))
               {
               YMDTPSC = ms.setSum(member[mi].year,member[mi].month,member[mi].day,member[mi].time,member[mi].point,member[mi].rClass);
               seatvalue = map.get(YMDTPSC);
               sv=seatvalue.length();
               
               j=0;
               for (int ai=0; ai<=sv; ai +=2,j++)         //a1 b1 c2 d2 적재
                  {
                     if (ai==sv)
                        break;
                     String z = seatvalue;      //a1b1c2d2
                     String x = seatvalue.substring(ai,ai+2);
                     if (x.equals(member[mi].rSeat))
                     {
                        seatvalue = z.replace(x, "");
                     }
                  }
               map.put(YMDTPSC,seatvalue);         //바뀐 value 돌려놓기
               year = cal.get(Calendar.YEAR);      //사용자로부터 입력받은 년도
               month = cal.get(Calendar.MONTH)+1;   //사용자로부터 입력받은 월
               CP.print(year, month);      //연월 받아서 달력출력

               nowYear = cal.get(Calendar.YEAR);  //현재의 년도
               nowMonth = cal.get(Calendar.MONTH)+1;         //현재의 월
               nowDay = cal.get(Calendar.DAY_OF_MONTH);  //현재의 일
               DS.getNowYear(nowYear);     //현재 년 불러오는 메소드
               DS.getNowMonth(nowMonth);   //현재 월 불러오는 메소드
               DS.getNowDay(nowDay);      //현재 일 불러오는 메소드

               DS.getYear(year);         //예매할 년도 불러오는 메소드(사용자에게 입력받은 년도)
               DS.getMonth(month);         //예매할 달 불러오는 메소드(사용자에게 입력받은 달)

               selectNum1 = CM.input();   //메뉴 1234중 선택한값 넘겨줌
               day = DS.input(selectNum1); //1234중 선택한 메뉴 실행 후 입력받은 날짜 반환
               System.out.printf("%d년 %d월 %d일",DS.year,DS.month,day);

               PPO.Pointinput();         //날짜팀 제작은 아니지만 이 위치에 넣어야 해서 삽입함.
               time = TT.timetable(year,month,day,PPO.result);
               
               c = SC.Check();            //좌석 어떤것 선택할것인지 묻는 클래스
               seatclass="에러";
               switch (c)                     //SC.Check값에 따라 퍼스트, 비즈니스, 이코노미 선택
               {
               case 1:  seatclass="퍼스트"; break;
               case 2:  seatclass="비즈니스"; break;
               case 3:  seatclass="이코노미"; break;
               case 4: break;
               }
               seatvalue="";            //값 초기화
               YMDTPSC = ms.setSum(year,month,day,time,PPO.result,seatclass);
               if (map.get(YMDTPSC)!=null)
               {
                  seatvalue = map.get(YMDTPSC);      //YMDTPSC에 따른 값 꺼내기(null처리)
               }
               
               
               sv=0;
               try                        //예외처리구문
               {
                  sv=seatvalue.length();
               }
               catch(NullPointerException e)
               {
                  sv=0;
               }
                        
               j=0;
               a=0;
               b=0;
               for (int ai=0; ai<=sv; ai +=2,j++)         //a1 b1 c2 d2 적재
               {
                  if (ai==sv)
                  {
                  break;
                  }
                  String z = map.get(YMDTPSC);      //a1b1c2d2

                  if (z.charAt(ai+1)-'0' !=0)
                  {
                     if (z.charAt(ai) == 'A')
                     {  
                        a = 0;
                        b = z.charAt(ai+1) - '0';
                     }
                     else if(z.charAt(ai) == 'B')
                     {
                        a = 1 ;
                        b = z.charAt(ai+1) - '0';
                     }
                     else if(z.charAt(ai) == 'C')
                     { 
                        a = 2 ;
                        b = z.charAt(ai+1) - '0';
                     }
                     else if(z.charAt(ai) == 'D')
                     {
                        a = 3 ;
                        b = z.charAt(ai+1) - '0';
                     }
                     else if(z.charAt(ai) == 'E')
                     {
                        a = 4 ;
                        b = z.charAt(ai+1) - '0';
                     }
                     else if(z.charAt(ai) == 'F')
                     {
                        a = 5 ;
                        b = z.charAt(ai+1) - '0';
                     }
                     else if(z.charAt(ai) == 'G')
                     {
                        a = 6 ;
                        b = z.charAt(ai+1) - '0';
                     }
                     else if(z.charAt(ai) == 'H')
                     {
                        a = 7 ;
                        b = z.charAt(ai+1) - '0';
                     }
                     else if(z.charAt(ai) == 'I')
                     {
                        a = 8 ;
                        b = z.charAt(ai+1) - '0';
                     }
                     else if(z.charAt(ai) == 'J')
                     {
                        a = 9 ;
                        b = z.charAt(ai+1) - '0';
                     }
                  SS.SetFBE(a,b);
                  }
               }
               
            
               do
               {
                  SS.SeatImpl();                  //좌석 보여주기
                  SS.print(seatclass);            //좌석 입력하기
               }
               while (SS.getcancel() == 1);
               
               seatvalue += SS.getc();
               map.put(YMDTPSC,seatvalue);

               APP.pay(PPO.result);
               PCC.calPrint(seatclass,SS.getc(),APP.getAFC(),APP.getLGC(),year,month,day,time,coupon);
               YMDTPSC = ms.setSum(year,month,day,time,PPO.result,seatclass);

               member[mi].setMember(TB.id,TB.pw,TB.mil,PPO.result,TB.name,PCC.seatName,1,seatclass,mi);   //예약멤버 배열
               member[mi].setYMDT(year,month,day,time);
               

               System.out.print("티켓을 출력하시겠습니까?(Y/N) :");
               tChoice = sc.next();                     

               if (tChoice.equals("Y") || tChoice.equals("y"))
               {
               Tic.YMDSetting(member[mi].year,member[mi].month,member[mi].day,member[mi].time);
               Tic.TicketPrint(member[mi].point,member[mi].rName,member[mi].rSeat,member[mi].rClass);      //티켓 출력
               }
               break;}
               else 
                  break;
            case 2: 
            System.out.print("정말로 예약을 취소하시겠습니까? (Y/N) : ");   
             realYn = sc.next();
            if (realYn.equals("Y") || realYn.equals("y"))
            {
               member[mi].sethasT(0); 
               YMDTPSC = ms.setSum(member[mi].year,member[mi].month,member[mi].day,member[mi].time,member[mi].point,member[mi].rClass);
               seatvalue = map.get(YMDTPSC);
               sv=seatvalue.length();
               
               j=0;
               for (int ai=0; ai<=sv; ai +=2,j++)         //a1 b1 c2 d2 적재
                  {
                     if (ai==sv)
                        break;
                     String z = seatvalue;      //a1b1c2d2
                     String x = seatvalue.substring(ai,ai+2);
                     if (x.equals(member[mi].rSeat))
                     {
                        seatvalue = z.replace(x, "");
                     }
                  }
               map.put(YMDTPSC,seatvalue);         //바뀐 value 돌려놓기

               System.out.println("예약이 취소되었습니다.");
               a=0;
               b=0;
               break;
            }
            else 
               break;
            
            case 3: 
               
            }
               }
            }
            else
              System.out.println("예약된 티켓이 없습니다.");
         break;
                                          //루틴 이해) 아이디에 예약된 티켓이 있을경우- 예약된 티켓이 있습니다. 출력합니까?
                                             //1-티켓 출력, 0-다시 처음화면으로
         
            case 3 :   seatvalue="";                  //값 초기화
                  year = cal.get(Calendar.YEAR);            //사용자로부터 입력받은 년도
                  month = cal.get(Calendar.MONTH)+1;         //사용자로부터 입력받은 월
                  CP.print(year, month);            //연월 받아서 달력출력
                  selectNum1 = CM.input();         //메뉴 1234중 선택한값 넘겨줌
                  day = DS.input(selectNum1);         //1234중 선택한 메뉴 실행 후 입력받은 날짜 반환
                  PPO.Pointinput();
                  time = TT.timetable(year,month,day,PPO.result);
                  c = SC.Check();            //좌석 어떤것 선택할것인지 묻는 클래스
                  seatclass = "에러";
                  switch (c)                     //SC.Check값에 따라 퍼스트, 비즈니스, 이코노미 선택
                  {
                     case 1: seatclass="퍼스트"; break;
                     case 2: seatclass="비즈니스"; break;
                     case 3: seatclass="이코노미"; break;
                     case 4: break;
                  }
                  YMDTPSC = ms.setSum(year,month,day,time,PPO.result,seatclass);
                  
                  if (map.get(YMDTPSC)!=null)
                  {
                     seatvalue = map.get(YMDTPSC);      //YMDTPSC에 따른 값 꺼내기(null처리)
                  }                              //잔여좌석조회
                  try                              //예외처리구문
                  {
                     sv=seatvalue.length();
                  }
                  catch(NullPointerException e)
                  {
                     sv=0;
                  }
                  a=0;
                  b=0;
                  j=0;
                  for (int ai=0; ai<=sv; ai +=2,j++)         //a1 b1 c2 d2 적재
                  {
                     if (ai==sv)
                     {
                     break;
                     }
                     String z = map.get(YMDTPSC);      //a1b1c2d2
                     //hasharr[j] = z.substring(ai,ai+2);   //a1, b1, c2, d2

                     //for (int ab=0;ai<=sv/2-1;ai++)
                     //{
                     if (z.charAt(ai+1)-'0' !=0)
                     {
                        if (z.charAt(ai) == 'A')
                        {  
                           a = 0;
                           b = z.charAt(ai+1) - '0';
                        }
                        else if(z.charAt(ai) == 'B')
                        {
                           a = 1 ;
                           b = z.charAt(ai+1) - '0';
                        }
                        else if(z.charAt(ai) == 'C')
                        { 
                           a = 2 ;
                           b = z.charAt(ai+1) - '0';
                        }
                        else if(z.charAt(ai) == 'D')
                        {
                           a = 3 ;
                           b = z.charAt(ai+1) - '0';
                        }
                        else if(z.charAt(ai) == 'E')
                        {
                           a = 4 ;
                           b = z.charAt(ai+1) - '0';
                        }
                        else if(z.charAt(ai) == 'F')
                        {
                           a = 5 ;
                           b = z.charAt(ai+1) - '0';
                        }
                        else if(z.charAt(ai) == 'G')
                        {
                           a = 6 ;
                           b = z.charAt(ai+1) - '0';
                        }
                        else if(z.charAt(ai) == 'H')
                        {
                           a = 7 ;
                           b = z.charAt(ai+1) - '0';
                        }
                        else if(z.charAt(ai) == 'I')
                        {
                           a = 8 ;
                           b = z.charAt(ai+1) - '0';
                        }
                        else if(z.charAt(ai) == 'J')
                        {
                           a = 9 ;
                           b = z.charAt(ai+1) - '0';
                        }
                     SS.SetFBE(a,b);
                     }
                  }
                  SS.SeatImpl();
                  
                  break;

            case 4 : break;
       }//switch문 종료
       i++;
      }//while문 종료
         
   }
}

