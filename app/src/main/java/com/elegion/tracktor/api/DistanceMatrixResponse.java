package com.elegion.tracktor.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DistanceMatrixResponse {

    private Status status;
    @SerializedName("origin_addresses")
    private List<String> originAddresses;
    @SerializedName("destination_addresses")
    private List<String> destinationAddresses;
    private List<RowsBean> rows;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<String> getOriginAddresses() {
        return originAddresses;
    }

    public void setOriginAddresses(List<String> originAddresses) {
        this.originAddresses = originAddresses;
    }

    public List<String> getDestinationAddresses() {
        return destinationAddresses;
    }

    public void setDestinationAddresses(List<String> destinationAddresses) {
        this.destinationAddresses = destinationAddresses;
    }

    public List<RowsBean> getRows() {
        return rows;
    }

    public void setRows(List<RowsBean> rows) {
        this.rows = rows;
    }

    public static class RowsBean {
        private List<ElementsBean> elements;

        public List<ElementsBean> getElements() {
            return elements;
        }

        public void setElements(List<ElementsBean> elements) {
            this.elements = elements;
        }

        public static class ElementsBean {

            private String status;
            private DurationBean duration;
            private DistanceBean distance;

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public DurationBean getDuration() {
                return duration;
            }

            public void setDuration(DurationBean duration) {
                this.duration = duration;
            }

            public DistanceBean getDistance() {
                return distance;
            }

            public void setDistance(DistanceBean distance) {
                this.distance = distance;
            }

            public static class DurationBean {
                private int value;
                private String text;

                public int getValue() {
                    return value;
                }

                public void setValue(int value) {
                    this.value = value;
                }

                public String getText() {
                    return text;
                }

                public void setText(String text) {
                    this.text = text;
                }
            }

            public static class DistanceBean {
                private int value;
                private String text;

                public int getValue() {
                    return value;
                }

                public void setValue(int value) {
                    this.value = value;
                }

                public String getText() {
                    return text;
                }

                public void setText(String text) {
                    this.text = text;
                }
            }
        }
    }

    public enum Status {
        /**
         * @see #OK – указывает, что ответ содержит корректный результат result.
         * @see #INVALID_REQUEST – указывает на недопустимый запрос.
         * @see #MAX_ELEMENTS_EXCEEDED – указывает, что произведение числа исходных точек и числа пунктов назначения превысило ограничение по запросу.
         * @see #OVER_QUERY_LIMIT – указывает, что служба получила слишком много запросов от вашего приложения в течение разрешенного периода.
         * @see #REQUEST_DENIED – указывает, что вашему приложению отказано в использовании службы Distance Matrix.
         * @see #UNKNOWN_ERROR – не удалось выполнить запрос Distance Matrix из-за ошибки сервера. Если повторить попытку, запрос может оказаться успешным.
         */
        OK,
        INVALID_REQUEST,
        MAX_ELEMENTS_EXCEEDED,
        OVER_QUERY_LIMIT,
        REQUEST_DENIED,
        UNKNOWN_ERROR,
    }
}
