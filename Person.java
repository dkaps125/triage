package triage;

abstract class Person {
	String name;
	int age;
	
	Person() {}
	
	Person(String n, int a) {
		this.name = n;
		this.age = a;
	}
}
