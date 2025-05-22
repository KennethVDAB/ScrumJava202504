package be.vdab.scrumjava202504.orders;

import be.vdab.scrumjava202504.products.ProductDTO;
import be.vdab.scrumjava202504.products.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class PickingServiceTest {

    private OrderRepository orderRepository;
    private ProductRepository productRepository;
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        orderRepository = mock(OrderRepository.class);
        productRepository = mock(ProductRepository.class);
        orderService = Mockito.spy(new OrderService(orderRepository, productRepository));
    }

    @Test
    void testGetOrderDetailsByOrderId_returnsCorrectPickingItems() {
        long orderId = 1L;
        long productId = 100L;

        // Bestelde hoeveelheid = 3
        OrderDetails orderDetails = new OrderDetails(1L, orderId, productId, BigDecimal.valueOf(3));
        when(orderRepository.getOrderDetailsByOrderId(orderId)).thenReturn(List.of(orderDetails));

        // Voorraad in het rek = 5
        ProductDTO location = new ProductDTO("A", 10, "Product A", 5, (int) productId);
        when(productRepository.findByArtikelId(productId)).thenReturn(List.of(location));

        // Roep de methode aan
        List<PickingItem> result = orderService.getOrderDetailsByOrderId(orderId);

        // Controleer
        assertEquals(1, result.size());

        PickingItem item = result.get(0);
        assertEquals("A", item.getShelf());
        assertEquals(10, item.getPosition());
        assertEquals("Product A", item.getName());
        assertEquals(3, item.getPickedQuantity()); // <= GEPICKTE hoeveelheid
        assertEquals(productId, item.getProductId());
    }

    @Test
    void testGetOrderDetailsByOrderId_choosesMostEfficientRoute() {
        long orderId = 1L;
        long productId = 100L;

        // Bestel 6 stuks
        OrderDetails orderDetails = new OrderDetails(1L, orderId, productId, BigDecimal.valueOf(6));
        when(orderRepository.getOrderDetailsByOrderId(orderId)).thenReturn(List.of(orderDetails));

        // 3 mogelijke locaties met variërende routekost en voorraad
        ProductDTO locatie1 = new ProductDTO("C", 20, "Product A", 2, (int) productId); // duurdste
        ProductDTO locatie2 = new ProductDTO("A", 5, "Product A", 3, (int) productId);  // goedkoopste
        ProductDTO locatie3 = new ProductDTO("B", 10, "Product A", 4, (int) productId); // middel

        when(productRepository.findByArtikelId(productId))
                .thenReturn(List.of(locatie1, locatie2, locatie3));


        List<PickingItem> result = orderService.getOrderDetailsByOrderId(orderId);

        // Controle: moet eerst A5 gebruiken (max 3), dan B10 (rest 3), C20 niet gebruiken
        assertEquals(2, result.size());

        PickingItem eerste = result.get(0);
        assertEquals("A", eerste.getShelf());
        assertEquals(5, eerste.getPosition());
        assertEquals(3, eerste.getPickedQuantity());

        PickingItem tweede = result.get(1);
        assertEquals("B", tweede.getShelf());
        assertEquals(10, tweede.getPosition());
        assertEquals(3, tweede.getPickedQuantity());
    }

    @Test
    void testResultIsSortedByShelfAndPosition() {
        long orderId = 4L;
        long productId = 103L;

        OrderDetails orderDetails = new OrderDetails(1L, orderId, productId, BigDecimal.valueOf(3));
        when(orderRepository.getOrderDetailsByOrderId(orderId)).thenReturn(List.of(orderDetails));

        ProductDTO locatie1 = new ProductDTO("B", 2, "Product", 1, 1);
        ProductDTO locatie2 = new ProductDTO("A", 2, "Product2", 1, 2);
        ProductDTO locatie3 = new ProductDTO("A", 1, "Product3", 1, 3);

        when(productRepository.findByArtikelId(productId)).thenReturn(List.of(locatie1, locatie2, locatie3));

        List<PickingItem> result = orderService.getOrderDetailsByOrderId(orderId);

        // Sortering is A1, A2, B2
        assertEquals("A", result.get(0).getShelf());
        assertEquals(1, result.get(0).getPosition());

        assertEquals("A", result.get(1).getShelf());
        assertEquals(2, result.get(1).getPosition());

        assertEquals("B", result.get(2).getShelf());
        assertEquals(2, result.get(2).getPosition());
    }

    @Test
    void testGetOrderDetailsByOrderId_returnsEmptyListWhenNoProducts() {
        long orderId = 103L;
        when(orderRepository.getOrderDetailsByOrderId(orderId)).thenReturn(List.of());

        List<PickingItem> result = orderService.getOrderDetailsByOrderId(orderId);

        assertTrue(result.isEmpty());
    }
    }

