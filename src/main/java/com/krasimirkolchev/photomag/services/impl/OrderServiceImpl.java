package com.krasimirkolchev.photomag.services.impl;

import com.krasimirkolchev.photomag.models.bindingModels.ExpOrdersDatesBindingModel;
import com.krasimirkolchev.photomag.models.entities.Order;
import com.krasimirkolchev.photomag.models.serviceModels.CartItemServiceModel;
import com.krasimirkolchev.photomag.models.serviceModels.OrderItemServiceModel;
import com.krasimirkolchev.photomag.models.serviceModels.OrderServiceModel;
import com.krasimirkolchev.photomag.models.serviceModels.UserServiceModel;
import com.krasimirkolchev.photomag.repositories.OrderRepository;
import com.krasimirkolchev.photomag.services.*;
import com.stripe.model.Charge;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.xssf.usermodel.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");


    private final OrderRepository orderRepository;
    private final UserService userService;
    private final ShoppingCartService shoppingCartService;
    private final OrderItemService orderItemService;
    private final ProductService productService;
    private final AddressService addressService;
    private final ModelMapper modelMapper;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, UserService userService, ShoppingCartService shoppingCartService,
                            OrderItemService orderItemService, ProductService productService, AddressService addressService, ModelMapper modelMapper) {
        this.orderRepository = orderRepository;
        this.userService = userService;
        this.shoppingCartService = shoppingCartService;
        this.orderItemService = orderItemService;
        this.productService = productService;
        this.addressService = addressService;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<OrderServiceModel> getAllOrders() {
        return this.orderRepository.findAll()
                .stream()
                .map(o -> this.modelMapper.map(o, OrderServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderServiceModel> getAllOrdersByUsername(String username) {
        return this.orderRepository.getOrdersByUser_UsernameOrderByPurchaseDateTime(username)
                .stream()
                .map(o -> this.modelMapper.map(o, OrderServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public OrderServiceModel createOrder(OrderServiceModel orderServiceModel) {
        Order order = this.modelMapper.map(orderServiceModel, Order.class);
        return this.modelMapper.map(this.orderRepository.save(order), OrderServiceModel.class);
    }

    @Override
    public OrderServiceModel generateOrder(Charge charge, Principal principal, String addressId) {
        UserServiceModel userServiceModel = this.modelMapper
                .map(this.userService.getUserByUsername(principal.getName()), UserServiceModel.class);

        OrderServiceModel orderServiceModel = new OrderServiceModel();
        List<OrderItemServiceModel> orderItems = generateOrderItems(userServiceModel.getShoppingCart().getItems());
        orderServiceModel.setOrderItems(orderItems);

        orderServiceModel.setUser(userServiceModel);
        orderServiceModel.setChargeId(charge.getId());
        orderServiceModel.setTotalAmount(userServiceModel.getShoppingCart().getTotalCartAmount());
        orderServiceModel.setAddress(this.addressService.getAddressById(addressId));
        this.createOrder(orderServiceModel);
        this.shoppingCartService.retrieveShoppingCart(userServiceModel.getShoppingCart());

        return orderServiceModel;
    }

    @Override
    public int hasOrdersForPeriod(ExpOrdersDatesBindingModel expOrdersDatesBM) {
        LocalDateTime from = LocalDateTime
                .parse(expOrdersDatesBM.getExpFrom() + "T00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        LocalDateTime to = LocalDateTime
                .parse(expOrdersDatesBM.getExpTo() + "T23:59", DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        return this.orderRepository.getOrdersByPurchaseDateTimeBetweenOrderByPurchaseDateTimeAsc(from, to).size();
    }

    private List<OrderItemServiceModel> generateOrderItems(List<CartItemServiceModel> cartItemsList) {
        return cartItemsList
                .stream()
                .map(ci -> {
                    OrderItemServiceModel orderItemServiceModel = this.modelMapper.map(ci, OrderItemServiceModel.class);
                    orderItemServiceModel.setOrderItem(this.productService.getProductById(ci.getItem().getId()));
                    return this.orderItemService.addOrderItem(orderItemServiceModel);
                })
                .collect(Collectors.toList());
    }

    @Override
    public File exportOrdersForPeriod(ExpOrdersDatesBindingModel expOrdersDatesBindingModel) throws IOException {

        XSSFWorkbook workbook = this.createExpOrderForPeriod(expOrdersDatesBindingModel);

        File file = new File("src/main/resources/static/files/exportedOrders.xlsx");

        FileOutputStream fos = new FileOutputStream(file, false);

        workbook.write(fos);

        workbook.close();
        fos.close();
        return file;
    }

    private XSSFWorkbook createExpOrderForPeriod(ExpOrdersDatesBindingModel expOrdersDatesBM) {
        LocalDateTime from = LocalDateTime
                .parse(expOrdersDatesBM.getExpFrom() + "T00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        LocalDateTime to = LocalDateTime
                .parse(expOrdersDatesBM.getExpTo() + "T23:59", DateTimeFormatter.ISO_LOCAL_DATE_TIME);


        List<OrderServiceModel> ordersToExport = this.orderRepository
                .getOrdersByPurchaseDateTimeBetweenOrderByPurchaseDateTimeAsc(from, to)
                .stream().map(o -> this.modelMapper.map(o, OrderServiceModel.class))
                .collect(Collectors.toList());


        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet();

        this.createMainRow(workbook, sheet);

        int rowIndex = 1;

        for (OrderServiceModel order : ordersToExport) {

            XSSFRow rowDate = sheet.createRow(rowIndex);

            this.createCell(rowDate, 1, formatter.format(order.getPurchaseDateTime()), null);


            rowIndex++;

            this.fillUserData(workbook, sheet, rowIndex, order);

            rowIndex++;

            for (OrderItemServiceModel orderItem : order.getOrderItems()) {
                this.fillOrderItemData(workbook, sheet, rowIndex, orderItem);

                rowIndex++;
            }

            rowIndex++;

            XSSFRow row2 = sheet.createRow(rowIndex);

            this.createCell(row2, 8, order.getTotalAmount(), null);

            rowIndex += 2;

        }


        return workbook;
    }

    private void fillOrderItemData(XSSFWorkbook workbook, XSSFSheet sheet, int rowIndex, OrderItemServiceModel orderItem) {
        XSSFRow row1 = sheet.createRow(rowIndex);

        XSSFCellStyle userInfoRowStyle = workbook.createCellStyle();
        userInfoRowStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(0, 203, 61)));
        userInfoRowStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        this.createCell(row1, 4, orderItem.getOrderItem().getName(), userInfoRowStyle);
        this.createCell(row1, 5, orderItem.getQuantity(), userInfoRowStyle);
        this.createCell(row1, 6, orderItem.getOrderItem().getPrice(), userInfoRowStyle);
        this.createCell(row1, 7, orderItem.getSubTotal(), userInfoRowStyle);
    }

    private void fillUserData(XSSFWorkbook workbook, XSSFSheet sheet, int rowIndex, OrderServiceModel order) {
        XSSFRow row = sheet.createRow(rowIndex);

        XSSFCellStyle userInfoRowStyle = workbook.createCellStyle();
        userInfoRowStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(11, 255, 85)));
        userInfoRowStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        this.createCell(row, 0, order.getUser().getFirstName(), userInfoRowStyle);
        this.createCell(row, 1, order.getUser().getLastName(), userInfoRowStyle);
        this.createCell(row, 2, order.getUser().getEmail(), userInfoRowStyle);
        this.createCell(row, 3, order.getAddress().toString(), userInfoRowStyle);
    }

    private void createMainRow(XSSFWorkbook workbook, XSSFSheet sheet) {
        XSSFRow mainRow = sheet.createRow(0);

        XSSFCellStyle mainRowStyle = workbook.createCellStyle();
        mainRowStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(196, 176, 176)));
        mainRowStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        this.createCell(mainRow, 0, "First Name", mainRowStyle);
        this.createCell(mainRow, 1, "Last Name", mainRowStyle);
        this.createCell(mainRow, 2, "Email", mainRowStyle);
        this.createCell(mainRow, 3, "Address", mainRowStyle);
        this.createCell(mainRow, 4, "Item Name", mainRowStyle);
        this.createCell(mainRow, 5, "Item Quantity", mainRowStyle);
        this.createCell(mainRow, 6, "Item Price", mainRowStyle);
        this.createCell(mainRow, 7, "Item Sub Total", mainRowStyle);
        this.createCell(mainRow, 8, "Order Total", mainRowStyle);
    }

    private void createCell(XSSFRow row, int cellIndex, String value, XSSFCellStyle style){
        XSSFCell cell = row.createCell(cellIndex);
        cell.setCellValue(value);
        if (style != null) {
            cell.setCellStyle(style);
        }
    }

    private void createCell(XSSFRow row, int cellIndex, int value, XSSFCellStyle style){
        XSSFCell cell = row.createCell(cellIndex);
        cell.setCellValue(value);
        if (style != null) {
            cell.setCellStyle(style);
        }
    }

    private void createCell(XSSFRow row, int cellIndex, double value, XSSFCellStyle style){
        XSSFCell cell = row.createCell(cellIndex);
        cell.setCellValue(value);
        if (style != null) {
            cell.setCellStyle(style);
        }
    }

}
