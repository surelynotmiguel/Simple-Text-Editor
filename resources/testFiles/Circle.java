public class Circle{
	private double radius;

	public Circle(double radius){ this.radius = radius; }

	public void setRadius(double radius){ this.radius = radius; }
	public double getRadius(){ return this.radius; }
	public double getArea(){ return (2 * Math.PI * (Math.pow(getRadius(), 2))); }

	public static void main(String[] args){
		Circle circle = new Circle(9.6);

		System.out.println("Area = " + String.format("%,.2f", circle.getArea()) + " u²");
	}
}
