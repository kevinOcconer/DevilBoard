package com.kevin.devilboard;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.kevin.devilboard.models.DevilException;
import com.kevin.devilboard.models.DevilFailedRequest;
import com.kevin.devilboard.models.DevilLog;
import com.kevin.devilboard.models.DevilRequest;
import com.kevin.devilboard.models.LogMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kevin on 12/8/17.
 */

public class KAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<LogMessage> messages;
    private Gson gson;
    private final String DEBUG = "DEBUG";
    private final String ERROR = "ERROR";
    private final String REQUEST = "REQUEST";
    private final String FAILED_REQUEST = "FAILED_REQUEST";
    private final String EXCEPTION = "EXCEPTION";
    private final String ALIVE = "ALIVE";
    private final String DEAD = "DEAD";

    private final int DEBUG_TYPE = 1;
    private final int ERROR_TYPE = 2;
    private final int REQUEST_TYPE = 3;
    private final int FAILED_REQUEST_TYPE = 4;
    private final int EXCEPTION_TYPE = 5;
    private final int ALIVE_TYPE = 6;
    private final int DEAD_TYPE = 7;

    private Context mContext;

    KAdapter(ArrayList<LogMessage> name, Context context) {
        this.mContext = context;
        messages = name;
        gson = new Gson();
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view;
        if (viewType == ALIVE_TYPE) { // for call layout
            view = LayoutInflater.from(mContext).inflate(R.layout.alive_view, viewGroup, false);
            return new AliveViewHolder(view);
        } else if (viewType == DEAD_TYPE) { // for email layout
            view = LayoutInflater.from(mContext).inflate(R.layout.dead_view, viewGroup, false);
            return new DeadViewHolder(view);
        } else if (viewType == DEBUG_TYPE) {
            view = LayoutInflater.from(mContext).inflate(R.layout.debug_view, viewGroup, false);
            return new DebugHolder(view);
        } else if (viewType == ERROR_TYPE) {
            view = LayoutInflater.from(mContext).inflate(R.layout.error_view, viewGroup, false);
            return new ErrorHolder(view);
        } else if (viewType == EXCEPTION_TYPE) {
            view = LayoutInflater.from(mContext).inflate(R.layout.exception_view, viewGroup, false);
            return new ExceptionHolder(view);
        } else if (viewType == REQUEST_TYPE) {
            view = LayoutInflater.from(mContext).inflate(R.layout.request_view, viewGroup, false);
            return new RequestHolder(view);
        } else {
            view = LayoutInflater.from(mContext).inflate(R.layout.failed_request_view, viewGroup, false);
            return new FailedHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == ALIVE_TYPE) {
            ((AliveViewHolder) holder).setMessage(messages.get(position).getJsonData());
        } else if (getItemViewType(position) == DEAD_TYPE) {
            ((DeadViewHolder) holder).setMessage(messages.get(position).getJsonData());
        } else if (getItemViewType(position) == DEBUG_TYPE) {
            ((DebugHolder) holder).setData(gson.fromJson(messages.get(position).getJsonData(), DevilLog.class));
        } else if (getItemViewType(position) == ERROR_TYPE) {
            ((ErrorHolder) holder).setData(gson.fromJson(messages.get(position).getJsonData(), DevilLog.class));
        } else if (getItemViewType(position) == EXCEPTION_TYPE) {
            ((ExceptionHolder) holder).setData(gson.fromJson(messages.get(position).getJsonData(), DevilException.class));
        } else if (getItemViewType(position) == FAILED_REQUEST_TYPE) {
            ((FailedHolder) holder).setData(gson.fromJson(messages.get(position).getJsonData(), DevilFailedRequest.class));
        } else if (getItemViewType(position) == REQUEST_TYPE) {
            ((RequestHolder) holder).setData(gson.fromJson(messages.get(position).getJsonData(), DevilRequest.class));
        }

    }

    class AliveViewHolder extends RecyclerView.ViewHolder {
        TextView message;

        AliveViewHolder(@NonNull View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.message);
        }

        public void setMessage(String msg) {
            message.setText(msg);
        }
    }

    class DeadViewHolder extends RecyclerView.ViewHolder {
        TextView message;

        DeadViewHolder(@NonNull View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.message);
        }

        public void setMessage(String msg) {
            message.setText(msg);
        }
    }

    class DebugHolder extends RecyclerView.ViewHolder {

        TextView type, message, filename, classname, linenumber, methodName;

        public DebugHolder(@NonNull View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.message);
            type = itemView.findViewById(R.id.type);
            filename = itemView.findViewById(R.id.file_name);
            classname = itemView.findViewById(R.id.class_name);
            linenumber = itemView.findViewById(R.id.line_number);
            methodName = itemView.findViewById(R.id.method_name);
        }

        public void setData(DevilLog devilLog) {
            type.setText("Log type : " + DEBUG);
            message.setText("Message " + devilLog.getMessage());
            filename.setText("FileName :  " + devilLog.getCodeLocation().getFileName());
            classname.setText("ClassName :  " + devilLog.getCodeLocation().getclassName());
            linenumber.setText("LineNumber :  " + devilLog.getCodeLocation().getLineNumber());
            methodName.setText("MethodName :  " + devilLog.getCodeLocation().getMethod() + "()");
        }
    }

    class ErrorHolder extends RecyclerView.ViewHolder {

        TextView type, message, filename, classname, linenumber, methodName;

        public ErrorHolder(@NonNull View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.message);
            type = itemView.findViewById(R.id.type);
            filename = itemView.findViewById(R.id.file_name);
            classname = itemView.findViewById(R.id.class_name);
            linenumber = itemView.findViewById(R.id.line_number);
            methodName = itemView.findViewById(R.id.method_name);
        }

        public void setData(DevilLog devilLog) {
            type.setText("Log type : " + ERROR);
            message.setText("Message " + devilLog.getMessage());
            filename.setText("FileName :  " + devilLog.getCodeLocation().getFileName());
            classname.setText("ClassName :  " + devilLog.getCodeLocation().getclassName());
            linenumber.setText("LineNumber :  " + devilLog.getCodeLocation().getLineNumber());
            methodName.setText("MethodName :  " + devilLog.getCodeLocation().getMethod() + "()");
        }
    }

    class ExceptionHolder extends RecyclerView.ViewHolder {

        TextView type, message, filename, classname, linenumber, methodName, exception, border;

        public ExceptionHolder(@NonNull View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.message);
            type = itemView.findViewById(R.id.type);
            filename = itemView.findViewById(R.id.file_name);
            classname = itemView.findViewById(R.id.class_name);
            linenumber = itemView.findViewById(R.id.line_number);
            methodName = itemView.findViewById(R.id.method_name);
            exception = itemView.findViewById(R.id.exception);
        }

        public void setData(DevilException devilLog) {
            type.setText("Log type : " + EXCEPTION);
            message.setText("Message " + devilLog.getMessage());
            filename.setText("FileName :  " + devilLog.getCodeLocation().getFileName());
            classname.setText("ClassName :  " + devilLog.getCodeLocation().getclassName());
            linenumber.setText("LineNumber :  " + devilLog.getCodeLocation().getLineNumber());
            methodName.setText("MethodName :  " + devilLog.getCodeLocation().getMethod() + "()");
            exception.setText("Exception :  " + devilLog.getException());
        }
    }

    class FailedHolder extends RecyclerView.ViewHolder {

        TextView url, message, filename, classname, linenumber, methodName;

        public FailedHolder(@NonNull View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.message);
            url = itemView.findViewById(R.id.url);
            filename = itemView.findViewById(R.id.file_name);
            classname = itemView.findViewById(R.id.class_name);
            linenumber = itemView.findViewById(R.id.line_number);
            methodName = itemView.findViewById(R.id.method_name);
        }

        public void setData(DevilFailedRequest devilLog) {
            url.setText(devilLog.getUrl());
            message.setText("Message  : " + devilLog.getErrorMessage());
            filename.setText("FileName :  " + devilLog.getCodeLocation().getFileName());
            classname.setText("ClassName :  " + devilLog.getCodeLocation().getclassName());
            linenumber.setText("LineNumber :  " + devilLog.getCodeLocation().getLineNumber());
            methodName.setText("MethodName :  " + devilLog.getCodeLocation().getMethod() + "()");
        }
    }

    class RequestHolder extends RecyclerView.ViewHolder {

        TextView url, code, size, localtime, servertime, border;

        public RequestHolder(@NonNull View itemView) {
            super(itemView);
            border = itemView.findViewById(R.id.border);
            url = itemView.findViewById(R.id.url);
            code = itemView.findViewById(R.id.code);
            size = itemView.findViewById(R.id.size);
            localtime = itemView.findViewById(R.id.localtime);
            servertime = itemView.findViewById(R.id.remotetime);
        }

        public void setData(DevilRequest devilLog) {
            try {
                border.setBackgroundColor(devilLog.getCode() > 200 ? Color.RED : Color.GREEN);
                url.setText(devilLog.getUrl());
                code.setText("Response Code :  " + devilLog.getCode());
                size.setText("Response Size :  " + devilLog.getSize());
                localtime.setText("Elapsed time :     " + devilLog.getElapsedTime() + " ms");
                servertime.setText("Server  time :     " + devilLog.getServerReqResTime() + " ms");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public int getItemViewType(int position) {
        switch (messages.get(position).getType()) {
            case DEBUG:
                return DEBUG_TYPE;
            case ERROR:
                return ERROR_TYPE;
            case REQUEST:
                return REQUEST_TYPE;
            case FAILED_REQUEST:
                return FAILED_REQUEST_TYPE;
            case EXCEPTION:
                return EXCEPTION_TYPE;
            case ALIVE:
                return ALIVE_TYPE;
            case DEAD:
                return DEAD_TYPE;
            default:
                return 0;
        }
    }


    @Override
    public int getItemCount() {
        return messages.size();
    }

}
