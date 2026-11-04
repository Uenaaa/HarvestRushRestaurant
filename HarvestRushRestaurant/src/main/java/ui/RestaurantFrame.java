package ui;

import command.CommandInvoker;
import command.PlantCropCommand;
import command.ServeOrderCommand;
import command.StartCookCommand;
import crop.Crop;
import crop.CropFactory;
import customer.Customer;
import manager.RestaurantManager;
import observer.RestaurantObserver;
import observer.RestaurantSubject;
import order.Order;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class RestaurantFrame extends JFrame implements RestaurantObserver {
    private final RestaurantManager manager;
    private final CommandInvoker invoker;

    private final JTextArea logArea = new JTextArea();
    private final DefaultListModel<String> growingModel = new DefaultListModel<>();
    private final DefaultListModel<String> customerModel = new DefaultListModel<>();
    private final DefaultListModel<String> orderModel = new DefaultListModel<>();
    private final JList<String> orderList = new JList<>(orderModel);
    private final JComboBox<String> cropCombo = new JComboBox<>(new String[]{"wheat", "veg", "tomato"});
    private final JLabel stockLabel = new JLabel();
    private final JLabel goldLabel = new JLabel();
    private final JCheckBox nonStickBox = new JCheckBox("不粘锅（烹饪 -1 回合）");
    private final JCheckBox seasonBox = new JCheckBox("调味台（售价 +3）");

    public RestaurantFrame(RestaurantSubject subject, RestaurantManager manager, CommandInvoker invoker) {
        this.manager = manager;
        this.invoker = invoker;

        setTitle("HarvestRush 后厨急单");
        setSize(900, 560);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JSplitPane splitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT, buildGardenPanel(), buildKitchenPanel());
        splitPane.setResizeWeight(0.38);

        add(splitPane, BorderLayout.CENTER);
        add(buildBottomPanel(), BorderLayout.SOUTH);

        subject.attach(this);
        refresh();
    }

    private JPanel buildGardenPanel() {
        JPanel panel = new JPanel(new BorderLayout(6, 6));
        panel.setBorder(BorderFactory.createTitledBorder("菜园"));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(new JLabel("选择作物："));
        top.add(cropCombo);
        JButton plantBtn = new JButton("播种");
        plantBtn.addActionListener(e -> plantCrop());
        top.add(plantBtn);

        JScrollPane growingScroll = new JScrollPane(new JList<>(growingModel));
        growingScroll.setBorder(BorderFactory.createTitledBorder("生长中"));

        panel.add(top, BorderLayout.NORTH);
        panel.add(growingScroll, BorderLayout.CENTER);
        panel.add(stockLabel, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel buildKitchenPanel() {
        JPanel panel = new JPanel(new BorderLayout(6, 6));
        panel.setBorder(BorderFactory.createTitledBorder("后厨"));

        JPanel stovePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        stovePanel.add(new JLabel("灶台："));
        nonStickBox.addActionListener(e -> {
            manager.nonStickEnabled = nonStickBox.isSelected();
            refresh();
        });
        seasonBox.addActionListener(e -> {
            manager.seasonedEnabled = seasonBox.isSelected();
            refresh();
        });
        stovePanel.add(nonStickBox);
        stovePanel.add(seasonBox);

        JPanel lists = new JPanel(new GridLayout(2, 1, 6, 6));
        JScrollPane customerScroll = new JScrollPane(new JList<>(customerModel));
        customerScroll.setBorder(BorderFactory.createTitledBorder("客人列表"));
        JScrollPane orderScroll = new JScrollPane(orderList);
        orderScroll.setBorder(BorderFactory.createTitledBorder("订单列表"));
        lists.add(customerScroll);
        lists.add(orderScroll);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton cookBtn = new JButton("开始烹饪");
        cookBtn.addActionListener(e -> startCook());
        JButton serveBtn = new JButton("上菜");
        serveBtn.addActionListener(e -> serveOrder());
        buttons.add(cookBtn);
        buttons.add(serveBtn);

        panel.add(stovePanel, BorderLayout.NORTH);
        panel.add(lists, BorderLayout.CENTER);
        panel.add(buttons, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel buildBottomPanel() {
        JPanel panel = new JPanel(new BorderLayout(6, 6));

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT));
        controls.add(goldLabel);
        JButton tickBtn = new JButton("下一计时单位");
        tickBtn.addActionListener(e -> {
            manager.timeTick();
            appendLog(">>> 推进时间片");
            refresh();
        });
        JButton undoBtn = new JButton("撤销操作");
        undoBtn.addActionListener(e -> {
            if (invoker.undoLast()) {
                appendLog(">>> 执行撤销");
            } else {
                appendLog(">>> 没有可撤销的操作");
            }
            refresh();
        });
        controls.add(tickBtn);
        controls.add(undoBtn);

        logArea.setEditable(false);
        JScrollPane logScroll = new JScrollPane(logArea);
        logScroll.setPreferredSize(new Dimension(0, 130));
        logScroll.setBorder(BorderFactory.createTitledBorder("日志"));

        panel.add(controls, BorderLayout.NORTH);
        panel.add(logScroll, BorderLayout.CENTER);
        return panel;
    }

    private void plantCrop() {
        String type = (String) cropCombo.getSelectedItem();
        Crop crop = new CropFactory().create(type);
        invoker.runCommand(new PlantCropCommand(crop, manager));
        refresh();
    }

    private void startCook() {
        Order order = getSelectedOrder();
        if (order == null) {
            appendLog(">>> 请先在订单列表中选择一个订单");
            return;
        }
        invoker.runCommand(new StartCookCommand(order, manager));
        refresh();
    }

    private void serveOrder() {
        Order order = getSelectedOrder();
        if (order == null) {
            appendLog(">>> 请先在订单列表中选择一个订单");
            return;
        }
        invoker.runCommand(new ServeOrderCommand(order, manager));
        refresh();
    }

    private Order getSelectedOrder() {
        int index = orderList.getSelectedIndex();
        if (index < 0 || index >= manager.orderList.size()) {
            return null;
        }
        return manager.orderList.get(index);
    }

    private void refresh() {
        goldLabel.setText("金币：" + manager.gold);
        stockLabel.setText(formatStock());

        growingModel.clear();
        for (Crop crop : manager.growingCrops) {
            growingModel.addElement(crop.getCropType() + "（剩余 " + crop.growTime + " 回合）");
        }

        customerModel.clear();
        int index = 0;
        for (Customer customer : manager.customerList) {
            customerModel.addElement((++index) + ". " + customer.getCustomerType()
                    + "（耐心 " + Math.max(0, customer.patience) + "）");
        }

        orderModel.clear();
        for (Order order : manager.orderList) {
            orderModel.addElement(formatOrder(order));
        }
    }

    private String formatOrder(Order order) {
        String state;
        if (order.done) {
            state = "可上菜";
        } else if (order.cooking) {
            state = "烹饪中，剩余 " + order.remainCookTime + " 回合";
        } else {
            int estimate = Math.max(1, order.strategy.getCookTime() + manager.getStove().getTimeModify());
            state = "待烹饪，预计 " + estimate + " 回合";
        }

        int priceModify = (order.cooking || order.done) ? order.priceModify : manager.getStove().getPriceModify();
        int income = (int) ((order.strategy.getBasePrice() + priceModify) * order.customer.tipRate);

        return order.strategy.getDishName() + " | " + order.customer.getCustomerType()
                + " | " + state + " | 收入 " + income;
    }

    private String formatStock() {
        if (manager.stock.isEmpty()) {
            return "库存：空";
        }
        StringBuilder sb = new StringBuilder("库存：");
        for (Map.Entry<String, Integer> entry : manager.stock.entrySet()) {
            sb.append(entry.getKey()).append("×").append(entry.getValue()).append("  ");
        }
        return sb.toString();
    }

    @Override
    public void update(String message) {
        appendLog(message);
    }

    private void appendLog(String message) {
        logArea.append(message + "\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }
}
