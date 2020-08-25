//package net.huansi.equipment.equipmentapp.activity.make_bills;
//
//import android.graphics.Color;
//import android.view.View;
//
//import net.huansi.equipment.equipmentapp.R;
//import net.huansi.equipment.equipmentapp.activity.BaseActivity;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import butterknife.BindView;
//import lecho.lib.hellocharts.formatter.ColumnChartValueFormatter;
//import lecho.lib.hellocharts.formatter.SimpleColumnChartValueFormatter;
//import lecho.lib.hellocharts.gesture.ContainerScrollType;
//import lecho.lib.hellocharts.gesture.ZoomType;
//import lecho.lib.hellocharts.model.Axis;
//import lecho.lib.hellocharts.model.AxisValue;
//import lecho.lib.hellocharts.model.Column;
//import lecho.lib.hellocharts.model.ColumnChartData;
//import lecho.lib.hellocharts.model.Line;
//import lecho.lib.hellocharts.model.LineChartData;
//import lecho.lib.hellocharts.model.PointValue;
//import lecho.lib.hellocharts.model.SubcolumnValue;
//import lecho.lib.hellocharts.model.ValueShape;
//import lecho.lib.hellocharts.model.Viewport;
//import lecho.lib.hellocharts.view.ColumnChartView;
//import lecho.lib.hellocharts.view.LineChartView;
//
//public class ShowChartActivity extends BaseActivity{
//    @BindView(R.id.column_chart)
//    ColumnChartView columnChart;
//    @BindView(R.id.line_chart)
//    LineChartView lineChart;
//
//    String[] date = {"10-22","11-22","12-22","1-22","6-22","5-23","5-22","6-22","5-23","5-22"};//X轴的标注
//    int[] score= {50,42,90,33,10,74,22,18,79,20};//图表的数据点
//    private List<PointValue> mPointValues = new ArrayList<PointValue>();
//    private List<AxisValue> mAxisXValues = new ArrayList<AxisValue>();
//    public final static String[] xValues = new String[]{"", "第1月", "第2月", "第3月", "第4月","第5月","第6月"};
//    private int month;
//    private SubcolumnValue subcolumnValue;
//    @Override
//    protected int getLayoutId() {
//        return R.layout.chart_show_main_activity;
//    }
//
//    @Override
//    public void init() {
//        getAxisXLables();//获取x轴的标注
//        getAxisPoints();//获取坐标点
//        initLineChart();//初始化折线图设置
//        initColumnChart();//初始化柱状图设置
//    }
//
//    private void initColumnChart() {
//        List<Column> columnList = new ArrayList<>(); //柱子列表
//        List<SubcolumnValue> subcolumnValueList;     //子柱列表（即一个柱子，因为一个柱子可分为多个子柱）
//        List<AxisValue> axisValues = new ArrayList<>();
//        for (int i = 1; i <= xValues.length-1; i++) {
//            subcolumnValueList = new ArrayList<>();
//            //获取数据处理
//            if (i == 1 || i == 0) {
//                subcolumnValue = new SubcolumnValue();
//                subcolumnValue.setValue(5.8f);
//                if (i <= month) {
//                    subcolumnValue.setColor(Color.RED);
//                } else {
//                    subcolumnValue.setColor(Color.YELLOW);
//                }
//                subcolumnValueList.add(subcolumnValue);
//            }
//            if (i == 2) {
//                subcolumnValue = new SubcolumnValue();
//                subcolumnValue.setValue(6.0f);
//                if (i <= month) {
//                    subcolumnValue.setColor(Color.RED);
//
//                } else {
//                    subcolumnValue.setColor(Color.YELLOW);
//                }
//                subcolumnValueList.add(subcolumnValue);
//            }
//            if (i >= 3) {
//                subcolumnValue = new SubcolumnValue();
//                subcolumnValue.setValue(7.0f);
//                if (i <= month) {
//                    subcolumnValue.setColor(Color.RED);
//                } else {
//                    subcolumnValue.setColor(Color.YELLOW);
//                }
//                subcolumnValueList.add(subcolumnValue);
//            }
//
//
//            Column column = new Column(subcolumnValueList);
//            columnList.add(column);
//            //是否有数据标注
//            column.setHasLabels(true);//☆☆☆☆☆设置列标签
//            //是否是点击圆柱才显示数据标注
//            column.setHasLabelsOnlyForSelected(true);
//            //TODO 这一步是能让圆柱标注数据显示带小数的重要一步
//            ColumnChartValueFormatter chartValueFormatter = new SimpleColumnChartValueFormatter(1);
//            column.setFormatter(chartValueFormatter);
//            //给x轴坐标设置描述
//            axisValues.add(new AxisValue(i - 1).setLabel(xValues[i]));
//        }
//
//
//        //图形数据加载
//        ColumnChartData columnChartData = new ColumnChartData(columnList);
//        columnChart.setZoomEnabled(true);//手势缩放
//        columnChart.setInteractive(true);//设置图表是可以交互的（拖拽，缩放等效果的前提）
//        columnChart.setZoomType(ZoomType.HORIZONTAL);//设置缩放方向
//
//        Axis axisX = new Axis(axisValues);//x轴
//        Axis axisY = new Axis();//y轴
//        //是否显示网格线
//        axisY.setHasLines(true);
//        axisY.hasLines();
//        axisX.setTextSize(12);
//
//                axisX.setName("时间/月");
//        //        axisY.setName(getResources().getString(R.string.expect_income_tariff));
//
//        axisX.setTextColor(Color.BLACK);
//        axisY.setTextColor(Color.BLACK);
//        //设置倾斜显示在柱子内部
//        //        axisX.setInside(true);
//        //        axisX.setHasTiltedLabels(true);
//
//        columnChartData.setFillRatio(0.5F);//参数即为柱子宽度的倍数，范围只能在0到1之间
//        //设置显示的数据背景、字体颜色
//        columnChartData.setValueLabelsTextColor(Color.BLACK);// 设置数据文字颜色
//        columnChartData.setValueLabelBackgroundEnabled(false);
//        columnChartData.setAxisXBottom(axisX);
//        columnChartData.setAxisYLeft(axisY);
//        //把数据放到控件中
//        columnChart.setColumnChartData(columnChartData);
//
//        //设置竖线
//        Viewport v = new Viewport(columnChart.getMaximumViewport());
//        v.top = 7.2f;
//        v.bottom = 5.4f;
//        columnChart.setMaximumViewport(v);
//        v.left = -0.5f;
//        v.right = 6;
//        columnChart.setCurrentViewport(v);
//    }
//
//    private void initLineChart() {
//        Line line = new Line(mPointValues).setColor(Color.parseColor("#FFCD41"));  //折线的颜色（橙色）
//        List<Line> lines = new ArrayList<Line>();
//        line.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.DIAMOND）
//        line.setCubic(true);//曲线是否平滑，即是曲线还是折线
//        line.setFilled(true);//是否填充曲线的面积
//        line.setHasLabels(true);//曲线的数据坐标是否加上备注
////      line.setHasLabelsOnlyForSelected(true);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
//        line.setHasLines(true);//是否用线显示。如果为false 则没有曲线只有点显示
//        line.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示（每个数据点都是个大的圆点）
//        lines.add(line);
//        LineChartData data = new LineChartData();
//        data.setLines(lines);
//
//        //坐标轴
//        Axis axisX = new Axis(); //X轴
//        axisX.setHasTiltedLabels(true);  //X坐标轴字体是斜的显示还是直的，true是斜的显示
//        axisX.setTextColor(Color.BLUE);  //设置字体颜色
//        axisX.setName("远东公司报表");  //表格名称
//        axisX.setTextSize(10);//设置字体大小
//        axisX.setMaxLabelChars(8); //最多几个X轴坐标，意思就是你的缩放让X轴上数据的个数7<=x<=mAxisXValues.length
//        axisX.setValues(mAxisXValues);  //填充X轴的坐标名称
//        //data.setAxisXBottom(axisX); //x 轴在底部
//        data.setAxisXTop(axisX);  //x 轴在顶部
//        axisX.setHasLines(true); //x 轴分割线
//
//        // Y轴是根据数据的大小自动设置Y轴上限(在下面我会给出固定Y轴数据个数的解决方案)
//        Axis axisY = new Axis();  //Y轴
//        axisY.setName("y轴");//y轴标注
//        axisY.setTextSize(10);//设置字体大小
//        data.setAxisYLeft(axisY);  //Y轴设置在左边
//        //data.setAxisYRight(axisY);  //y轴设置在右边
//
//
//        //设置行为属性，支持缩放、滑动以及平移
//        lineChart.setInteractive(true);
//        lineChart.setZoomType(ZoomType.HORIZONTAL);
//        lineChart.setMaxZoom((float) 2);//最大方法比例
//        lineChart.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
//        lineChart.setLineChartData(data);
//        lineChart.setVisibility(View.VISIBLE);
//        /**注：下面的7，10只是代表一个数字去类比而已
//         * 当时是为了解决X轴固定数据个数。见（http://forum.xda-developers.com/tools/programming/library-hellocharts-charting-library-t2904456/page2）;
//         */
//        Viewport v = new Viewport(lineChart.getMaximumViewport());
//        v.left = 0;
//        v.right = 7;
//        lineChart.setCurrentViewport(v);
//    }
//
//    /**
//     * 设置X 轴的显示
//     */
//    private void getAxisXLables(){
//        for (int i = 0; i < date.length; i++) {
//            mAxisXValues.add(new AxisValue(i).setLabel(date[i]));
//        }
//    }
//    /**
//     * 图表的每个点的显示
//     */
//    private void getAxisPoints() {
//        for (int i = 0; i < score.length; i++) {
//            mPointValues.add(new PointValue(i, score[i]));
//        }
//    }
//
//
//}
