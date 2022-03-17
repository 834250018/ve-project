package cn.ve.base.pojo;

public class RequestHeaderHolder {

    private static final ThreadLocal<RequestHeader> holder = ThreadLocal.withInitial(RequestHeader::new);

    public static void set(RequestHeader header) {
        holder.set(header);
    }

    public static void clear() {
        holder.remove();
    }

    public static RequestHeader get() {
        return holder.get();
    }
}