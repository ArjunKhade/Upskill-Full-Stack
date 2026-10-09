SourceCode --{Compile}
             |
MSIL/CIL/IL(Intermediate Language)   or(Byte code injava)
             |
{Common Language Runtime(CLR)}--JIT(IL to Native Code)

O=null; {Non Determnistic finalisation -->We dont know when Distructor will call 
               We should not write any code in destructor}

.Net framework  = .Net base classes +CLR +utilities like(CSC)

Project Mono -> Linux
Xamarin -->Mobile

.Net Core -->.Net core works on all other platform   but .Net framework not
                --Open-source
                --Cross-platform
                --Lightweight
                --Extensible
               --Upto 2.2 only support ASP.NET MVC and Apis
               --v3+ supports Winforms and WPF also
                --V3---Directly Jump to V5
              
.Net Framework -- 4.8 Last release

.Net5 course Covers 

Assembly manifest --List of details about assembly

References ==Import in java
{}-namespace

Genration in garbage collection( Younger 0-1-2 Older)

************************************************
using System;

namespace MyFirstApp
{
    class Program
    {
        static void Main(string[] args)
        {
            Console.WriteLine("Hello World");
            Console.ReadLine();  
        }
    }
}

************************************************
*Property:Concept:
*************************************************
Class BasicProperty{
	private int p3;  //small case 
	public int P3   //property Uppercase
	 {
	    set {
  	           p3 = value;
  	          }
 	    get {
 	           return p3; 
   	          }
	  }
    }
***********************************************
public int P3 { get; set; }   //shortest way to declare property compiler will write set and get 

To verify Open Dev command prompt VS 2019    and fire this command --   ILDASM

***********************************************************
Optional Parameter --When value is not passed take this values;
Contructor :
  public Class(int I =10,int P2=20, int P3=30)
   { 
       this.I=I;
       this.P2=P2;
       this.P3=P3;
   }

Object Initilizer:
      Class o2 = new Class (10,20,30) { P4=40, P5=50, P6=60 }
      Class o2 = new Class () { P4=40, P5=50, P6=60 }
      Class o2 = new Class { P4=40, P5=50, P6=60 }
      Class o2 = new Class (10,20,30) 
      Class o2 = new Class { I=40, P2=50, P3=60 }
     
      
Static :
         Class loading time
         Console - class is static 

static Class1(){ 
         static i = 12;
       }

**Inheritance:
                -Code reusability
                -single inheritance is allowed in .net
public class BaseClass{ public int i;}
public class DerivedClass  :  BaseClass{ public int j};

**constructor: call ctr of base class

pubic DerivedClass(int i, int j ) : base(i){
         this.j=j;
     }

***************************************
Access Specifier: availability

**public -- everywhere
**private -- same class
**protected -- same class or derived class

**internal -- same class   or  same assembly(same project)
**protected internal -- same class  or  derived class or  same assembly(same project)
**private protected -- same class  or   derived class which is in the same assembly (if it is in diff                                             assembly it doesnt aacess)
***default :
At class level --private 
At namespace level--internal
      
**************************************************************************
**OverLoading--diff signature   

**MethodHidding--same signature--new keyword

**Overriding--override keyword--Method must be Virtual Method In Base class--Late bounding
                         --run time polymorphishm--In java all method  are virtual but in .net its not.

**Virtual Method --virtual methods are late bound--at run time decided which method should be called
**All Abstract method are Virtual methods.

**sealed keyword--stop method further to override--sealed override

<Time Stamp> 6/8/2022     -8:00-9:00   method overloading/overriding/hidding  

**Pure virtual function = Function which have only declaration no body.
**Abstract class--> need not have abstract method in it. but Abstract method inside in class then class      must be abstract.

**Sealed Class is exactly Opposite of AbstractClass.
public sealed class {}

**Interface--like abstract class with all abstract method.but no concreate implementation
                    --all method public and abstract
*******************************************************************************
***Ref and value type
      -All classes and theire varient like Interfaces,delegates,Object,String,array are example of ref type.
      -All structs and enums are value type a value type are stored in stack.
      -All enum are internally  int and all int are struct type.

**String --is exception to ref type beacuse it is immutable
              --it create new obj every time

****Data type*********************************************************************
**value type
int -4-System.Int32(CTS Data type)
uint-positive-4
short-2-System.Int16
ushort-2
long-8
ulong-8
byte-1- System.Byte
sbyte-1
char-2

float -4-low precise-System.Single(CTS type of float* IMP)
double-8-System.Double
decimal-16-high precision

bool- true/false -System.Boolean

**Ref type
string --C# datatype  --String CTS data type
object

swap two number demo

<TimeStamp> 12:15  diff beetween pointer and refrence  6-08-2022

***Refrences are safe pointers

C-Value/Pointer
C++-Value/ref/Pointer

init-to initialise variable

ref--pass by ref -need to be initilize before it used
out--same to ref--but out variable must be init in function
********************************Day4************************************
**Struct --You cant create paremeter-less contr in struct.
**enum --Set of constant--are related in same way,instead of creating seprate constant create as enum.
              --all enum are int --all int are struct

**Boxing--int-> Object
**Unboxing--Object->Int

**nullable
     int? i =10; //this can hold value of nullable type
	// Nullable<int> x1 = new Nullable<int>(); 
          
**Dispose()--method of --IDisposable Interface--to automatically realese resouces

*************************Array******************************************

all array are inherited from Array class
Instead of creating number of variable -- array support Index based operation 

API --Of array

Array.IndexOf(arr,30)--return index or -1
Array.LastIndexOf(arr,3)
Array.BinarySearch(arr,30)
Array.Clear(arr,0,arr.length)
Array.Copy(arr,arr2,arr.length)
Array.ConstrainedCopy(arr,0,arr2,0,arr.length)
Array.Reverse()
Array.sort()--sort in ascemding order

Sort-- Employee --Implement IComparable Interface--and sorting array

******Array 2D*****************************************************************

int [,] arr = new int [3,2];//2D aaray
int[,,] arr1 = new int [3,2,2];//3D array
int[,,,] arr1 = new int [3,2,2,5];//4D array

Lenght--3*2=6
Rank= No of dimentions 2D =2

**jagged array 
int [][] arr = new int [4][];

*************************Generics********************************************

**Code is same but datatype is different use Generics 

class MyStack<T> where T : class/struct/className/InterFaceName/new()/ClassName,InterFaceName,new()
                    
					             new()--class must be have no param constructor



**********************************Collections********************************************
using.System.Collections;
**Interface---IEnumerable-ICollection-IList-IDictionary 

**Class-- 
Stack
Queue--Enque and Deque
ArrayList
HashTable
SortedList

Add-Remove-Count(Property)---> Three things are always in any collection


*****************************Generics Collections*************************************************

using.System.Collections.Generic;//namespace

List<T>
Stack<T>
Queue<T>
Dictionary<T>

***************************Delegate ***********************************************************
to delegate  -- to assign

System.Delegate ---Class 

public delegate void Del1();//create delegate class

            Del1 objDel1 = Display;
			objDel1();
			objDel1 += show;
			objDel1 -= Display;
			
**ReadyMade --delegate  =>   --Action--delegate with no return type
                                               --Func --delegate with return type
				                               --Predicate--delegate with return type bool and parameter Int
			
			

*********************************************************************************************
//ano method
            Func<int, int, int> o2 = delegate (int a, int b) { return a + b; };
            Console.WriteLine(o2(10, 20));
			
//Lambda 

            Func<int, int> o4 = a => a * 2;
            Console.WriteLine(o4(20));

            Func<int, int, int> o5 = (a, b) => a + b;
            Console.WriteLine(o5(20,10));

            Predicate<int> o6 = a => a % 2 == 0;
            Console.WriteLine(o6(11));			
			
********************************Exception************************************************
		Exception                                                                                    ApplicationException
		
SystemException                                                                          
ArithmaticException , FormatException , NullRefrenceException        
DivideByZeroException
		
************************Event Handling*********************************************

**10/8/2022  => 8:00 - 9:00   Event 
 
 
***Callback function --Now function is over you can read the value;or you can return the value;

******************************Linq************************************************
(Seperate file for linq and PLinq)
*****************************Threading**********************************************************************************************
  
 Foreground thread --Main thead waits to finish Foreground thread to complete task (by default all thead Foreground)
 
 Background thread --t1.IsBackground = true;  (To make thread background)           

 t1.join --wait to t1 thread to complete (waiting call to t1 to join back)
 
 CLR --Allocate time slice for each thread --Depend on priority
 
 t1.priority = threadPriority.Highest---it will finish first
 t1.Abort()--abort thread in ceratin condition  
              t1.start --if thrad is aborted it will rhtow exception
			  
 t1.threadState --stopped/stopRequested/suspended/running/background/suspendRequest/waitSleepJoin
 {t1.suspend --pause thread
 t1.resume --start from this point   -- no longer in use}
 
 
 await --> non blocking waiting call
 
 **************************File***************************
 
 ***Serialization - converting to stream of byte array
 
 **Reflection --Reading the content of assembly
                    --at runtime you can dynamically create object of class
					--Reflection.emit generate assembly at runtime
	
*********************************Database Connectivity*****************
**Managed--run by CLR

	System.Data.sqlClient
	System.Data.Odbc
	System.Data.Oledb
	
	
	sqlConnection
	         |
	sqlCommand
	         |
****sqlDataReader  --is a connected, read only,forward(unidirectional) only set of record.
					          -- it is also called Firehose cursor.It is fatest way to read record.
	
ExecuteScalar()-Return single record--return object
ExecuteReader()-Return Multiple record
                      -dr.Read()---t/f
					  -dr.["EmpNo"]-return object
	                  -dr.Close();//using (....) we can autoclose the connection -IDisposable
					-dr.NextResult()		  
					
MultipleActiveResultSets=true  --(MARS) --add this statement to connection String--(to get emp details in each dept)	

		
System.Data.DataSet

sqlDataAdapter--fill and update in database
                      --when you need to create dataSet
					  
		
DataSet--is a disconnected, updatable, xml set of record.
           --	collection of DataTable	
		   --DataRelation--ForeingKey
		   

DataView--allows filtering and sorting dataTable,dataView is based on single dataTable it has same structure as dataTable
		                   
		   
DataTable--Table-- single table record -- having DataColumn and DataRow		   
		                 --DataTable does't support Sorting,Filtering,
					
					
***SqlDataAdapter
	
dataAdapter.fill(ds,"Emps")--Open connection and close 	
 $"Dept No = {TextBox1.data}";--String interpolation
 
 ******************************Shared Assembly*************************************************
 
 
step 1:			 Generate a key pair (can be done at command line or in Project properties   , .snk --strong name key file)
			   command line -># sn -k mykey1.snk

step 2:			(to generate a strong named asembly, assembly must be signed with a key pair)
			Signing is done in the project properties.

step 3:			(Only assemblies that have a "strong name" can be put into the GAC)
			Generate an Assembly with a strong name.(Build an assembly)

step 4:			Install the assembly in the Global Assembly Cache (GAC)
			command line -> gacutil /i Asm1.Dll   --to install 
									 gacutil /u Asm1.Dll      --to unistall
									 
****************
									 
(Use Visual Studeo Dev Command prompt)									 
1.	create 	project      -- Class Library(.net Framework)
2.Project Properties - signing  tick on sign the assembly
3.Click on Build--Build Solution
4.Go to this folder =>  C:\Users\DELL\OneDrive\Desktop\DotNet\ActsSharedAssemby\ActsSharedAssemby\bin\Debug\{ActsSharedAssemby.dll}
5.fire command	#gacutil /i ActsSharedAssemby.dll       => to install assembly
6.create  new client project  console
7.add refrences browse-select C:\Users\DELL\OneDrive\Desktop\DotNet\ActsSharedAssemby\ActsSharedAssemby\bin\Debug\{ActsSharedAssemby.dll}   ActsSharedAssemby.dll
8. Add this in main method:   ActsSharedAssemby.Class1 o = new ActsSharedAssemby.Class1();
											Console.WriteLine(o.Hello());
											Console.ReadLine();
											
9.	click on ActsSharedAssemby refrence -properties set copy local -false						

*****************************************************************************			
*************************Asp.Net MVC*******************************************

Asp.Net MVC--- Model View Controller
					--to achieve sepration of concerns
					--Model -class which represent data
					--View -presenting content through UI
					--Controller-component handle user interaction work with model and select a view to render,Initial entry point 
       
Create new Web Project

Web.config  --web app configuration
TODO----ActionResult
			---create layout
			--crate view which uses your layout


_ViewStart.cshtml

@-->Razor code
@-->C# code ,Print value of @j--actual value of j
@: --->html code

	   
***ViewBag.Id ---Pass controller to view
						--TypeCast not needed
						
***ViewData-->Key value pair--key-->String ,Value-->Object
					--TypeCasting is needed
					--Scope --> current request ,RedirectToAction-->new request--lost data
ViewData ["key"]=obj

**TempData-->Redirecting data will be not destroyyed

	   
*********************************************************************

Name-- should be match model binding	   

10:53 Model Mapping with Db 

web.config-->write database connection details
Scalability-->No of users

Session --> Interaction beetween client and server,Start when client send first request & end at TimeOut-->Time given to session which would end the session if request is not given in that time
             -session Cookie -->stored session Id

Cookies -->small textfile which are stored in browser

ViewModel-->Additional class which has extra properties than Model

============================EntityFramework======================

DbContext ---> class related to db
DbSet --> collection 

DbFirst
 Db is already present
 classes are generated
 
Code First
 Classes are already present 
 Db is generated
 
Model First
 GUI - do the modelling ( pictorially repesent entities) 
 generate the db 
 generate the classes

db first 
class Department{} 
class Employee{}
class EmployeesDbContext : DbContext
DbSet Employees; DbSet

**********************************************WCF******************************************
12:15 --20/08/2022 -->Hello

WCF--->Windows communication foundation--->All services at one place
clt+f5    --> run the services

RestFull (WCF) web service ----->    webHttpBinding

***********************************RestApi**************************

MVC -->CRUD


21/08/2022 -->9:50 
RestApi without EntityFramework


21/08/2022 -->10:45
RestApi With EntityFramework
DbFirst

*****************************LabExamPractise********************

Asp.net  web Application -->MVC -->create New Project
************************************************************
DBFirst--->Steps
1.Create New Project --> C#--Windows--Web--ASP.NET WEB Application (.net framework)--EFDBFirst--MVC--Untik Configure Http
2.Tools--NuGet Package manager--Manage Nuget Package manager--Browse--type EntityFramework--select EntityFramework (EF
)--Select Project Name--click on install--ok--i accept--Successfully installed 'EntityFramework 6.4.4' to EFDFirst--close Nuget tab
3.Click Project menu --add new item--search --entity --select ADO.NET Entitty Data Model--Add--EF Designer from Database--Next--New Connection--Data Source Microsoft SQL Server --Server name --(localdb)\MsSqlLocalDb--select Database name--TestConnection--Ok-- tik on save connection setting in web.config file (ActsJuly22Entities)--Next--Table --select tables--Finish
4.Build Solution
5.Controller --Add a Controller-- select MVC5 Controller with view EntityFramework--Model Class --{select Model Class}--Data Contest Class--{select ActsJulyEntites}--Click on Add
6.Add in route config {Employee} controller name --run 

*************************************************************

CodeFirst-->Steps  D13:19:08:2022 --  20:38
1.Add EntityFramework dependency from nuget package manager

2.Create Model in Model Folder
*******
using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Web;

namespace CodeFirst.Models
{
    public class Employee
    {
        [Key]
        public int EmpNo { get; set; }
        public string Name { get; set; }
        public decimal Basic { get; set; }
        public int DeptNo { get; set; }

        public virtual Department Department { get; set; }
    }

    public partial class Department
    {
       
        [Key]
        public int DeptNo { get; set; }
        public string DeptName { get; set; }
       public virtual ICollection<Employee> Employees { get; set; }
    }

    public partial class ActsDbContext : System.Data.Entity.DbContext
    {
        public ActsDbContext() : base("name=cn")

        // database will be named as given in the connectionStrings section in web.config
        {
        }

        //public ActsDbContext() : base("DatabaseName")
        //    // database will be called "DatabaseName"

        //{
        //}

        //public ActsDbContext() : base()  
        //    //database will be called EFCodeFirst.Models.ActsDbContext
        //    //same name if no cons given
        //{
        //}

        public virtual System.Data.Entity.DbSet<Department> Departments { get; set; }
        public virtual System.Data.Entity.DbSet<Employee> Employees { get; set; }
    }
}
***********
3.Do Entry in web.config(belove app setting)
<connectionStrings>
    <add name="cn" providerName="System.Data.SqlClient" connectionString="Data Source=(localdb)\MsSqlLocalDb;Initial Catalog=Acts;Integrated Security=True" />
  </connectionStrings>
4.Build Solution
5.Add Controller of Entity Framework
6.Create Controller oF Department to avoid Error Forene key constraint
7.Create Controller for Employees



**************************************WebApi*******************************
RestApi without EntityFramework

1.Create ASP.NET WEB Application (.net framework)--WebApi--create new Project
2.Model--Add class--eg.Employee(copy from  modelBindingWithDbCode )
3.Add Controller --Web API --Controller with read and wrte actions(3rd)
4.Build Project Run check on Postman
***********************************************************

RestApi with EntityFramework

1.Create ASP.NET WEB Application (.net framework)--WebApi--create new Project
2.Project--Add new Item --type entity--select ADO.Net Entitty data model--Ef Designer from DB--new connection--server Name-- (localdb)\MsSqlLocalDb--Database name--ActsJuly22--Select req Table
3.Add Controller--Web APi-- Select Controller with action Entity Framework--Add Model class and Data Context class
4.Build and Run check on Postman
