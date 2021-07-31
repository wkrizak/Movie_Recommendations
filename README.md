# Movie_Recommendations
Provides the user with a list of movie recommendations based on viewing history.
## Description
The initial window lists out 10 movies for the user to rate. The user can input ratings via slider objects, or choose not to rate certain movies.
Using the three buttons on the bottom of the window, the user can:
* Choose to rate an additional 10 movies.
* View all recommended movies.
* Filter the list of recommended movies based on certain criteria. The program currently allows the user to select a specific genre and/or certain release years.  

The window of movie recommendations lists up to 10 movies based on the weighted average ratings from other critics.
The algorithm compares the user's ratings to a dataset of movie critics to determine the most similar critics.
Ratings from more similar critics are weighted more heavily than others to determine the top recommended movies.
## Installation Comments
* This project utilizes Apache Commons CSV. Download here: [Apache Commons CSV](https://commons.apache.org/proper/commons-csv/download_csv.cgi)  
## License
MIT License

Copyright (c) [2021] [William Krizak]

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
