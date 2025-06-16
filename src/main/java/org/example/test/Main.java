
package org.example.test;


import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.example.test.Entity.Category;
import org.example.test.Entity.Product;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        SessionFactory factory = new Configuration().configure().buildSessionFactory();
        Session session = factory.openSession();
        Scanner sc = new Scanner(System.in);

        boolean loop = true;

        while (loop){

            System.out.println("\n\n\033[33m----------------------------------------\033[0m");
            System.out.println("hello");
            System.out.println("1. Add Product");
            System.out.println("2. Update Product");
            System.out.println("3. Delete Product");
            System.out.println("4. View All Products");
            System.out.println("5. Add Category");
            System.out.println("6. Update Category");
            System.out.println("7. Delete Category");
            System.out.println("8. View All Categories");
            System.out.println("9. Search Products by Price Range");
            System.out.println("10. Search Products by Name");
            System.out.println("11. Get Top 5 Cheapest Products");
            System.out.println("12. Get Products by Category");
            System.out.println("13. Exit");
            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            // Add Product
            if(choice == 1){
                List<Category> list = session.createQuery("From Category", Category.class).list();
                if(list.isEmpty()){
                    System.out.println("No categories yet to insert the product");
                    continue;
                }

                System.out.print("Enter Name: ");
                String name = sc.nextLine();
                System.out.print("Enter Price: ");
                int price = sc.nextInt();
                sc.nextLine();

                for(Category c:list){
                    System.out.println(c.getId()+ " " + c.getName());
                }

                System.out.print("Enter the Id of category: ");
                int categoryId = sc.nextInt();
                sc.nextLine();

                Category c = session.get(Category.class, categoryId);

                Product p = new Product(name, price, c);

                Transaction tx = session.beginTransaction();
                session.persist(p);
                c.addProduct(p);
                tx.commit();

                System.out.println("Product added successfully");

            }


            // Update Product
            if(choice == 2){

                List<Product> list = session.createQuery("From Product", Product.class).list();

                if(list.isEmpty()){
                    System.out.println("No Products Yet");
                    continue;
                }

                for(Product p : list){
                    System.out.println(p.getId() + " " + p.getName());
                }

                System.out.print("Enter the Id of Product: ");
                int productId = sc.nextInt();
                sc.nextLine();

                Product p = session.get(Product.class, productId);

                System.out.print("Enter New Name: ");
                String name = sc.nextLine();
                System.out.print("Enter New Price: ");
                int price = sc.nextInt();
                sc.nextLine();

                Transaction tx = session.beginTransaction();
                p.setName(name);
                p.setPrice(price);
                tx.commit();

                System.out.println("Product Updated Successfully");
            }


            // Delete Product
            if(choice == 3){
                List<Product> list = session.createQuery("From Product", Product.class).list();

                if(list.isEmpty()){
                    System.out.println("No Products Yet");
                    continue;
                }

                for(Product p : list){
                    System.out.println(p.getId() + " " + p.getName());
                }

                System.out.print("Enter the Id of Product: ");
                int productId = sc.nextInt();
                sc.nextLine();

                Product p = session.get(Product.class, productId);

                Transaction tx = session.beginTransaction();
                p.getCategory().removeProduct(p);
                session.remove(p);
                tx.commit();

                System.out.println("Product Removed Successfully");
            }


            // View All Products
            if(choice == 4){
                List<Product> list = session.createQuery("From Product", Product.class).list();

                if(list.isEmpty()){
                    System.out.println("No Products Yet");
                    continue;
                }

                for(Product p : list){
                    System.out.println(p);
                }

            }


            // Add Category
            if(choice == 5){
                System.out.print("Enter Name: ");
                String name = sc.nextLine();

                Category c = new Category(name);

                Transaction tx = session.beginTransaction();
                session.persist(c);
                tx.commit();

                System.out.println("Category added successfully");
            }


            // Update Category
            if(choice == 6){
                List<Category> list = session.createQuery("From Category", Category.class).list();
                if(list.isEmpty()){
                    System.out.println("No categories yet");
                    continue;
                }

                for(Category c:list){
                    System.out.println(c.getId()+ " " + c.getName());
                }

                System.out.print("Enter the Id of the category to remove: ");
                int categoryId = sc.nextInt();
                sc.nextLine();

                Category c = session.get(Category.class, categoryId);

                System.out.print("Enter New Name: ");
                String name = sc.nextLine();

                Transaction tx = session.beginTransaction();
                c.setName(name);
                tx.commit();

                System.out.println("Category Updated");


            }


            // Delete Category
            if(choice == 7){
                List<Category> list = session.createQuery("From Category", Category.class).list();
                if(list.isEmpty()){
                    System.out.println("No categories yet");
                    continue;
                }

                for(Category c:list){
                    System.out.println(c.getId()+ " " + c.getName());
                }

                System.out.print("Enter the Id of the category to remove: ");
                int categoryId = sc.nextInt();
                sc.nextLine();

                Category c = session.get(Category.class, categoryId);

                Transaction tx = session.beginTransaction();
                session.remove(c);
                tx.commit();

                System.out.println("Category Removed Successfully");

            }


            // View All Categories
            if(choice == 8){

                List<Category> list = session.createQuery("From Category", Category.class).list();
                if(list.isEmpty()){
                    System.out.println("No categories yet");
                    continue;
                }

                for(Category c:list){
                    System.out.println(c);
                }

            }


            // Search Products by Price range
            if(choice == 9){

                List<Product> list = session.createQuery("From Product", Product.class).list();
                if(list.isEmpty()){
                    System.out.println("No Products Yet");
                    continue;
                }

                System.out.print("Enter Lower Value: ");
                int lower = sc.nextInt();
                System.out.print("Enter Upper Value: ");
                int upper = sc.nextInt();
                sc.nextLine();

                HibernateCriteriaBuilder builder = session.getCriteriaBuilder();
                CriteriaQuery<Product> query = builder.createQuery(Product.class);

                Root<Product> root = query.from(Product.class);

                Predicate p1 = builder.between(root.get("price"), lower, upper);
                query.where(p1);

                Query<Product> p = session.createQuery(query);
                List<Product> productList = p.list();

                if(productList.isEmpty()){
                    System.out.println("No products in this price rangge");
                }
                else{
                    for(Product pr : productList){
                        System.out.println(pr);
                    }
                }

            }


            // Search Product By Name
            if(choice == 10){
                System.out.print("Enter Product Name: ");
                String name = sc.nextLine();

                String sql = "Select * from Product where name like :n";
                Query<Product> query = session.createNativeQuery(sql, Product.class);
                query.setParameter("n", (name+"%"));
                List<Product> list = query.getResultList();

                if(list.isEmpty()){
                    System.out.println("No Product with this name");
                }
                else{
                    for(Product p : list){
                        System.out.println(p);
                    }
                }
            }


            // Get Top 5 Cheapest Products
            if(choice == 11){

                String sql = "Select * from Product order by price limit 5";
                List<Product> list = session.createNativeQuery(sql, Product.class).getResultList();

                if(list.isEmpty()){
                    System.out.println("No Products Yet");
                }
                else{
                    for(Product p : list){
                        System.out.println(p);
                    }
                }

            }


            // Get Products By Category
            if(choice == 12){

                List<Category> list = session.createQuery("From Category", Category.class).list();
                if(list.isEmpty()){
                    System.out.println("No categories yet");
                    continue;
                }

                for(Category c:list){
                    System.out.println(c.getId()+ " " + c.getName());
                }

                System.out.print("Enter the Id of the category: ");
                int categoryId = sc.nextInt();
                sc.nextLine();

                Category c = session.get(Category.class, categoryId);

                List<Product> list1 = c.getProduct();

                if(list1.isEmpty()){
                    System.out.println("No Product with this name");
                }
                else{
                    for(Product p : list1){
                        System.out.println(p);
                    }
                }

            }


            // Exit
            if(choice == 13){
                loop = false;
                System.out.println("Byeeee");
            }


        }

        factory.close();

    }
}