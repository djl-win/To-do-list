package com.lele.comp5216_assignment1.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;
import com.lele.comp5216_assignment1.R;
import com.lele.comp5216_assignment1.entity.ShopItem;
import com.lele.comp5216_assignment1.repository.ShopItemRepository;

public class AddItemDialog extends DialogFragment {

    // 回调接口，当dialog关闭时执行这些操作。
    public interface OnDialogDismissListener {
        void onDialogDismissed();
    }

    // 回调接口，当dialog关闭时执行这些操作。
    public interface OnDialogShowListener {
        void onDialogShowed();
    }

    private OnDialogShowListener nListener;
    private OnDialogDismissListener mListener;
    TextInputEditText editText;
    Button dateSelectButton;
    ImageButton addItemButton;
    MaterialDatePicker<Long> datePicker;
    Calendar calendar;
    SimpleDateFormat dateFormat;
    String todayDate;
    ShopItemRepository shopItemRepository;
    ShopItem shopItem;
    
    // 页面初始化时，加载回调接口监听器的资源
    public void setOnDialogDismissListener(OnDialogDismissListener listener) {
        this.mListener = listener;
    }

    public void setOnDialogShowListener(OnDialogShowListener listener) {
        this.nListener = listener;
    }

    /**
     * onCreate:
     * 目的: 与 Activity 的 onCreate 类似，这是 Fragment 或 DialogFragment 的生命周期开始时被调用的方法。
     * 调用时机: 当 Fragment 或 DialogFragment 被创建时被调用，这是创建过程中的一个早期阶段。
     * 常见操作: 在这里，你可以进行一些初始化工作，如设置 Fragment 的样式和属性。
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置对话框的风格和主题
        setStyle(DialogFragment.STYLE_NORMAL, R.style.HalfScreenDialogStyle);

        // 获取数据库repository仓库
        if (getActivity() != null) {
            shopItemRepository = new ShopItemRepository(getActivity().getApplication());
        }
        // 初始化一个shopItem
        shopItem = new ShopItem();
        setCancelable(true); // 设置对话框可以被取消
    }

    /**
     * onCreateDialog:
     * 目的: 专为 DialogFragment 设计，它用于创建并返回与该 DialogFragment 关联的 Dialog 实例。
     * 调用时机: 在 DialogFragment 需要显示对话框时被调用。
     * 返回值: 它返回一个 Dialog 对象，该对象决定了 DialogFragment 显示时的外观和行为。
     * 常见操作: 你通常会在这里创建并设置一个 AlertDialog 或其他类型的 Dialog，然后返回它。
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // 创建对话框
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        if (dialog.getWindow() != null) {
            // 1. 设置软键盘模式，打开dialog的时候，软键盘自动弹出,并且当小键盘收回时，布局不改变
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
            // 设置dialog尺寸，并且居于下部
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = (int)(getResources().getDisplayMetrics().heightPixels * 0.51); // Set height to 80% of screen height
            dialog.getWindow().setLayout(width, height);
            dialog.getWindow().setGravity(Gravity.BOTTOM);
        }
        dialog.setCanceledOnTouchOutside(true);  // 点击对话框外部区域关闭对话框
        return dialog;
    }

    /**
     * onCreateView:
     * 目的: 这个方法是为了初始化和返回你的 Fragment 或 DialogFragment 的视图。
     * 调用时机: 当系统需要为 Fragment 创建视图时被调用。
     * 返回值: 它返回一个 View，这个 View 是 Fragment 或 DialogFragment 的 UI。
     * 常见操作: 在这里，你通常会使用 LayoutInflater 来将一个 XML 布局文件转化为 Android 视图组件。
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 将对话框的布局文件转化为视图
        View view = inflater.inflate(R.layout.add_item_dialog, container, false);
        // 初始化视图组件
        initViews(view);
        // 设置监听器
        setListeners();
        return view;
    }

    /**
     * 目的: 这个方法是为了执行那些在视图创建后需要进行的操作。
     * 调用时机: 紧接着 onCreateView 之后被调用，此时 onCreateView 返回的视图已经完全创建且返回。
     * 参数: 它有一个 View 参数，这个 View 就是 onCreateView 返回的视图。
     * 常见操作: 你通常会在这里进行视图绑定、设置视图监听器或者其他需要在视图创建后进行的操作。
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 获取焦点
        if (editText != null) {
            editText.requestFocus();
            // 手动打开软键盘
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
            }
        }
    }

    /**
     * onStart:
     * 目的: 表示 Fragment 或 DialogFragment 已经可见，但尚未处于前台或处于活跃状态。
     * 调用时机: 当 Fragment 或 DialogFragment 从创建状态进入可见状态时被调用。
     * 常见操作: 在这里，你可以执行需要在组件变得对用户可见时执行的操作。
     * 对于 Activity：
     *
     * onStart:
     * 目的: 表示 Activity 已经可见。
     * 调用时机: 当 Activity 从创建状态进入可见状态时被调用。
     * 常见操作: 在这里，你可以执行需要在组件变得对用户可见时执行的操作，例如启动一些动画或注册特定的监听器等。
     * 在生命周期中的顺序为：
     * onCreate() → onStart() → onResume() (现在组件处于活跃状态并与用户互动)
     * 对于 DialogFragment，如果你想在对话框变得对用户可见时执行某些操作，可以覆盖 onStart() 方法并在其中添加你的代码。
     **/


    // 获取xml中资源
    private void initViews(View view) {
        //获取今天的日期
        getTodayDate();
        editText = view.findViewById(R.id.add_item_edit_text);
        dateSelectButton = view.findViewById(R.id.btn_date_picker);
        addItemButton = view.findViewById(R.id.add_item_button);
    }

    // 设置监听器
    private void setListeners() {
        // 日期监听器
        datePickerListener();
        // 输入改变监听器
        textChangeListener();
        // 添加新购物清单项监听器
        createNewItemListener();
    }

    // 监听日期选择器
    private void datePickerListener() {
        dateSelectButton.setText(todayDate);
        // 为日期选择器添加监听时间
        dateSelectButton.setOnClickListener(v -> {
            // 当选择日期按钮被点击时，调用方法，使对话框消失
            showDatePicker();
        });
    }

    // 弹出日期选择器
    private void showDatePicker(){
        // 创建日期选择器实例
        datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select a date")
                .build();

        // 设置选择日期的监听器
        datePicker.addOnPositiveButtonClickListener(selection -> {
            // 在这里处理用户选择的日期
            // selection 参数是选定日期的时间戳
            // 可以根据需要进行处理，例如显示选定的日期等
            // 将时间戳转化为Date对象
            // 将时间戳转化为Date对象
            Date selectedDate = new Date(selection);

            // 使用SimpleDateFormat格式化日期
            todayDate = dateFormat.format(selectedDate);

            // 在Button上显示所选日期
            dateSelectButton.setText(todayDate);
        });

        // 显示日期选择器
        datePicker.show(getParentFragmentManager(), "datePicker");
    }

    // 监听textview
    private void textChangeListener() {
        //监听文本输入框的字是否有变化（注意，有时候第一次不检测，这里手动检测）
        checkEditTexts(editText.getText());
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 在此处不执行任何操作
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 在此处不执行任何操作
            }

            @Override
            public void afterTextChanged(Editable s) {
                checkEditTexts(s);
            }
        });
    }

    // 检查输入框的内容是否变化
    private void checkEditTexts(Editable s) {
        if (s != null && s.length() > 0) {
            // 当EditText有内容时，恢复ImageButton原色
            addItemButton.setColorFilter(null);
            addItemButton.setEnabled(true);
        } else {
            // 当EditText没有内容时，设置SVG的颜色为灰色
            addItemButton.setColorFilter(R.color.dialog_background_color);
            addItemButton.setEnabled(false);
        }
    }

    // 监听创建新的购物清单项
    private void createNewItemListener() {
        //监听添加新item按钮
        addItemButton.setOnClickListener(v -> {
            if (editText.getText() != null) {

                String inputText = editText.getText().toString().trim();
                String input = inputText + "    " + todayDate;
                //设置添加的商品名
                shopItem.setShopItemName(inputText);
                //设置添加商品日期
                shopItem.setShopItemDate(todayDate);
                shopItemRepository.insertShopItem(shopItem);
                dismiss();
            }
        });
    }

    // 获取今天的日期
    private void getTodayDate() {
        // 获取今天的日期
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("yyyy.MM.dd", Locale.getDefault());
        todayDate = dateFormat.format(calendar.getTime());
    }

    //dialog打开时，显示阴影
    @Override
    public void onResume() {
        super.onResume();
        if(nListener != null){
            nListener.onDialogShowed();
        }
    }

    //dialog关闭时显示阴影
    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (mListener != null) {
            mListener.onDialogDismissed();
        }
    }
    

}
