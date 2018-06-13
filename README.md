#  Android Easy View Content

AEVC  provides easy way to fill simple views contents with JSON data.

## Features
 * TextView, ImageView, CheckBox views instances supported.
 * RecycleView adapter supported.

Android 4.0+ support

**Current version: 0.1.0**

## Usage

### Acceptable URIs examples
``` java
"http://site.com/image.png"
```
### Acceptable JSON format
``` java
JSONObject
"{'title': 'hello'}"
JSONArray
{{'title': 'hello'},{'title': 'hello2'}}
```

## Non reusable views
#### Activity
``` java
EasyViewContent easyViewContent = new EasyViewContent(this, getWindow().getDecorView().getRootView());
```
#### Fragment
``` java
EasyViewContent easyViewContent = new EasyViewContent(this, getView());
```
``` java
// An example of locally created JSON data.
JSONObject jsonObject = new JSONObject();
jsonObject.put("textview_value", "EasyViewContent");
jsonObject.put("imageview_value", "https://avatars0.githubusercontent.com/u/26068897?s=200&v=4");
//{"textview_value": "EasyViewContent", "imageview_value" : "https://avatars0.githubusercontent.com/u/26068897?s=200&v=4"}
```
``` java
// Create references of inflated view elements.

ArrayList<Integer> view_ids = new ArrayList<>();
ArrayList<String> values = new ArrayList<>();

// Id's of elements inflated within layout
ids.add(R.id.view_1);
ids.add(R.id.image_1);

// JSON key names reference.
values.add("textview_value");
values.add("imageview_value");
```

##### Both lists must be linear reference
```
This means, ID must be linked by it's own value at JSON object with the JSON key name.
```
![Linear Reference](https://codersmx.s3.amazonaws.com/public/linear-reference.png)


## Reusable views (RecycleView)
``` java
// Inflate RecycleView
RecyclerView recyclerView = findViewById(R.id.list);

// locally created json array.
JSONArray jsonArray = new JSONArray();
jsonArray.put(jsonObject);  // Same jsonObject we created before.
jsonArray.put(jsonObject);  // twice for this example.

// Instantiate adapter for recycle view.
RecyclerView.Adapter adapter = easyViewContent.recycleView(
		this, // Activity instance.
        jsonArray, // JSON Object array.
		R.layout.list_row,  // Row view id.
		ids_recycleview,  // View id's.
		values_recycleview, // JSON key names.
		);

recyclerView.setAdapter(adapter);
```
## RecycleView adapter extra configurations
The adapter accept click, long click, default row position and custom image loader(CIL).
``` java
new EasyViewContentRecycleViewAdapter(
        this,
		jsonArray,
		R.layout.list_row,
		ids_recycleview,
		values_recycleview,
		// Custom image loader flag.
		// Click.
		// LongClick.
		// Default Position (0 by default).

);
```
### The loader can accept these configurations:
* CIL + Click + LongClick + DP.
* CIL + Click + DP.
* CIL + LongClick + DP.
* CIL + DP.
* CIL.
* Click + LongClick + DP.
* Click + LongClick + DP.
* Click + DP.
* LongClick + DP.
* Click.
* LongClick.
* DP.

#### Custom Image Loader.
##### Activity
Activity must extends **EasyViewContentActivity** and override the following method.
``` java
@Override
public void setUpCustomImageLoader(ImageView imageView, String url){
    try {
        // This is just an example of a custom image loader.
		imageView.setImageBitmap(new exampleImageLoader().execute(url).get());
  }catch (Exception e){
        Log.e(MainActivity.class.getSimpleName(), e.getLocalizedMessage());
  }
}
```
Adapter configuration
``` java
new EasyViewContentRecycleViewAdapter(
        this,
		jsonArray,
		R.layout.list_row,
		ids_recycleview,
		values_recycleview,
		true
);
```
##### Fragment
Activity must extends **EasyViewContentFragment** and override the same method as above.

The adapter configuration
``` java
new EasyViewContentRecycleViewAdapter(
        activity,
		jsonArray,
		R.layout.list_row,
		ids_recycleview,
		values_recycleview,
		this
);
```
#### Normal / Long click listener
``` java
new EasyViewContentRecycleViewAdapter.ClickListener() {
	      @Override
		  public void onClickView(int position) {
		    // Code here.
		  }
	    }

OR

new EasyViewContentRecycleViewAdapter.LongClickListener() {
	      @Override
		  public void onLongClickView(int position) {
		    // Code here.
		  }
	    }
```

## License

    Copyright 2016-2018 CodersMX

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.