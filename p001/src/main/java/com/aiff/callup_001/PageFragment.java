package com.aiff.callup_001;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.concurrent.ExecutionException;

public class PageFragment extends Fragment implements View.OnTouchListener {
    static final String ARGUMENT_PAGE_NUMBER = "arg_page_number";

    int pageNumber;
    View view1;
    View view2;

    static PageFragment newInstance(int page) {
        PageFragment pageFragment = new PageFragment();
        Bundle arguments = new Bundle();
        arguments.putInt(ARGUMENT_PAGE_NUMBER, page);
        pageFragment.setArguments(arguments);
        return pageFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageNumber = getArguments().getInt(ARGUMENT_PAGE_NUMBER);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (pageNumber == 0) {

            view1 = inflater.inflate(R.layout.fragment, null);
            Button btn1 = (Button) view1.findViewById(R.id.button1);
            btn1.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    String[] args = new String[6];

                    args[0] = "login";

                    TextView tv1 = (TextView) view1.findViewById(R.id.textView5);
                    EditText login = (EditText) view1.findViewById(R.id.phone_number);
                    EditText password = (EditText) view1.findViewById(R.id.password);

                    args[1] = String.valueOf(login.getText());
                    args[2] = String.valueOf(password.getText());

                    try {

                        String res = new ConnectServer().execute(args).get();
                        if (res == "1") {
                            Intent intentMain = new Intent(getActivity(), MainPageActivity.class);
                            startActivity(intentMain);
                        }
                        else tv1.setText("Incorrect Login / Password");

                    } catch (InterruptedException e) {
                        tv1.setText("An error occurred. Please, try again later.");
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        tv1.setText("An error occurred. Please, try again later.");
                        e.printStackTrace();
                    }

                    return false;
                }

            });

            return view1;

        } else {

            view2 = inflater.inflate(R.layout.fragment2, null);
            Button btn2 = (Button) view2.findViewById(R.id.button2);
            Button btn3 = (Button) view2.findViewById(R.id.button3);

            btn2.setOnTouchListener(this);
            btn3.setOnTouchListener(this);

            TextView tv2 = (TextView) view2.findViewById(R.id.textView2);
            TextView tv3 = (TextView) view2.findViewById(R.id.textView6);

            String[] args = new String[6];
            args[0] = "getId";

            try {
                String res = new ConnectServer().execute(args).get();
                tv2.setText(res);
            } catch (InterruptedException e) {
                tv2.setText("");
                tv3.setText("Can not connect to the server. Try again later.");
                e.printStackTrace();
            } catch (ExecutionException e) {
                tv2.setText("");
                tv3.setText("Can not connect to the server. Try again later.");
                e.printStackTrace();
            }

            return view2;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            String[] args = new String[6];
            String res;

            switch (v.getId()) {

                case R.id.button2:

                    args[0] = "getId";

                    TextView tv2 = (TextView) view2.findViewById(R.id.textView2);
                    TextView tv3 = (TextView) view2.findViewById(R.id.textView6);

                    try {
                        res = new ConnectServer().execute(args).get();
                        tv2.setText(res);
                    } catch (InterruptedException e) {
                        tv2.setText("");
                        tv3.setText("Can not connect to the server.");
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        tv2.setText("");
                        tv3.setText("Can not connect to the server.");
                        e.printStackTrace();
                    }

                    break;


                case R.id.button3:

                    TextView tv4 = (TextView) view2.findViewById(R.id.textView7);

                    EditText regName = (EditText) view2.findViewById(R.id.editText);
                    EditText regEmail = (EditText) view2.findViewById(R.id.editText2);
                    EditText regPass = (EditText) view2.findViewById(R.id.editText3);
                    EditText regConfPass = (EditText) view2.findViewById(R.id.editText4);
                    TextView regLogin = (TextView) view2.findViewById(R.id.textView2);

                    args = new String[6];
                    args[0] = "register";
                    args[1] = String.valueOf(regLogin.getText());
                    args[2] = String.valueOf(regPass.getText());
                    args[3] = String.valueOf(regEmail.getText());
                    args[4] = String.valueOf(regName.getText());

                    if (String.valueOf(regPass.getText()).equals(String.valueOf(regConfPass.getText()))) {

                        if (!args[2].equals("") && !args[3].equals("") && !args[4].equals("")) {

                            try {
                                res = new ConnectServer().execute(args).get();
                                if (res.equals("-2"))
                                    tv4.setText("Invalid phone number. Try again.");
                                if (res.equals("-1")) tv4.setText("An error occurred. Try again.");
                                if (res.equals("1")) tv4.setText("Ok.");
                            } catch (InterruptedException e) {
                                tv4.setText("System error occured. Try again later.");
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                tv4.setText("System error occured. Try again later.");
                                e.printStackTrace();
                            }

                        }

                        else {
                            regPass.setText("");
                            regConfPass.setText("");
                            tv4.setText("The fields are not filled");
                        }

                    }

                    else {
                        regPass.setText("");
                        regConfPass.setText("");
                        tv4.setText("Password do not match");
                    }

                    break;
            }

        }

        return false;
    }
}
