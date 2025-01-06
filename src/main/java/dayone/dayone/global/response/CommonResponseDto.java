package dayone.dayone.global.response;

public record CommonResponseDto<D>(
    int code,
    String message,
    D data
) {

    public static <D> CommonResponseDto<D> forSuccess(final int code, final String message, final D data) {
        return new CommonResponseDto<>(code, message, data);
    }

    public static <D> CommonResponseDto<D> forSuccess(final int code, final String message) {
        return new CommonResponseDto<>(code, message, null);
    }

    public static <D> CommonResponseDto<D> forFailure(final int code, final String message, final D data) {
        return new CommonResponseDto<>(code, message, data);
    }

    public static <D> CommonResponseDto<D> forFailure(final int code, final String message) {
        return new CommonResponseDto<>(code, message, null);
    }
}
