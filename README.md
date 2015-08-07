
### Framgia RSS Reader (Android)
#### Mô tả

1. Mô tả chức năng
 - Ứng dụng đọc tin RSS thông tin về du lịch với các danh mục là các thành phố hoặc quốc gia trên thế giới
 - Dữ liệu được lấy tại trang http://www.telegraph.co.uk/travel/rssfeeds/
 - Người dùng có thể lưu cache toàn bộ danh mục hoặc một bài viết trong danh mục để xem offline
 - Sử dụng Google Maps giúp người dùng xem bản đồ thành phố và vẽ đường đi từ điểm hiện tại đến thành phố đó trên map

2. Yêu cầu ứng dụng
 - Màn hình chính có 2 tab: News và H
 - Tại tab News chọn một vài category có sẵn là các thành phố
 - Hiển thị list item: đầy đủ nhưng thông tin: thumbnail (nếu có), title, short description… .Có thể lựa chọn giữa 2 dạng hiển thị là list hoặc gridview.
 - Từ trang list show trang detail tương ứng từng item.
 - Từ cả trang list và trang detail, người dùng có thể chọn favourite, sẽ lưu trữ lại những thông tin của những favourite để có thể xem offline (với trang list thì phải cache cả ảnh thumb, trang detail chỉ cần cache html , chưa cần cache ảnh hay style).
 - Tại cả list và detail đề có 1 button để khi người dùng click vào sẽ hiển thị bản đồ move vào thành phố đó. (tên thành phố là tên category fix ở phần 1).
 - Tại bản đồ có thể hiện route từ vị trí hiện tại đến thành phố đó!
 - Tab History tương tự tab news, nhưng là hiển thị dữ liệu offline, truy vấn từ local!

3. Kết quả
 ![Alt text](https://raw.githubusercontent.com/phamxuanlu/frss/task%237Redesign/screenshots/home-frss.png "Framgia RSS")