package ma.xproce.inventorymanagementsystem.dao.repositories;

import jakarta.transaction.Transactional;
import ma.xproce.inventorymanagementsystem.dao.entities.Sale;
import ma.xproce.inventorymanagementsystem.dao.entities.UserIMS;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

@Transactional
public interface SaleRepository extends JpaRepository<Sale, Integer>{
    Page<Sale> findByReferenceContainsAndCreatedBy(String keyword,UserIMS user, Pageable pageable);
    List<Sale> findByCreatedBy(UserIMS user);
    @Query("SELECT COALESCE(COUNT(s), 0) FROM Sale s WHERE s.saleStatus = 'Pending' AND s.createdBy = :user")
    int totalPendingSales(UserIMS user);

    @Query("SELECT COALESCE(COUNT(s), 0) FROM Sale s WHERE s.saleStatus = 'Completed' AND s.createdBy = :user AND s.refunded = false")
    int totalCompletedSales(UserIMS user);

    @Query("SELECT COALESCE((SUM(s.product.price) * SUM(s.quantity)), 0) FROM Sale s WHERE s.createdBy = :user")
    double totalSalesRevenue(UserIMS user);

    // Income
    @Query("SELECT COALESCE((SUM(s.product.price) * SUM(s.quantity)), 0) FROM Sale s WHERE s.date >= :startOfWeek AND s.createdBy = :user")
    double calculateWeeklyIncome(LocalDate startOfWeek, UserIMS user);

    @Query("SELECT COALESCE((SUM(s.product.price) * SUM(s.quantity)), 0) FROM Sale s WHERE s.date >= :startOfMonth AND s.createdBy = :user")
    double calculateMonthlyIncome(LocalDate startOfMonth, UserIMS user);

    @Query("SELECT COALESCE((SUM(s.product.price) * SUM(s.quantity)), 0) FROM Sale s WHERE s.date >= :startOfYear AND s.createdBy = :user")
    double calculateYearlyIncome(LocalDate startOfYear, UserIMS user);

    // Expenses
    @Query("SELECT COALESCE((SUM(s.product.cost) * SUM(s.quantity)), 0) FROM Sale s WHERE s.date >= :startOfMonth AND s.createdBy = :user")
    double calculateMonthlyExpenses(LocalDate startOfMonth, UserIMS user);

    @Query("SELECT COALESCE((SUM(s.product.cost) * SUM(s.quantity)), 0) FROM Sale s WHERE s.date >= :startOfWeek AND s.createdBy = :user")
    double calculateWeeklyExpenses(LocalDate startOfWeek, UserIMS user);

    @Query("SELECT COALESCE((SUM(s.product.cost) * SUM(s.quantity)), 0) FROM Sale s WHERE s.date >= :startOfYear AND s.createdBy = :user")
    double calculateYearlyExpenses(LocalDate startOfYear, UserIMS user);

    // Top Selling Products
    @Query("SELECT COALESCE(s.product.id,0) FROM Sale s GROUP BY s.product.id ORDER BY SUM(s.quantity) DESC")
    List<Integer> findTop5SellingProductIds(Pageable pageable);

}