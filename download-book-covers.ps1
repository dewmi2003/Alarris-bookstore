# PowerShell Script to Download Book Covers
# This script downloads book covers from Open Library API

$booksDir = "src\main\resources\static\images\books"

# Ensure directory exists
if (!(Test-Path $booksDir)) {
    New-Item -ItemType Directory -Path $booksDir -Force
}

Write-Host "📚 Downloading book covers..." -ForegroundColor Cyan

# Book cover URLs (using Open Library and placeholder services)
$books = @{
    "to-kill-a-mockingbird.jpg" = "https://covers.openlibrary.org/b/isbn/9780061120084-L.jpg"
    "1984.jpg" = "https://covers.openlibrary.org/b/isbn/9780451524935-L.jpg"
    "great-gatsby.jpg" = "https://covers.openlibrary.org/b/isbn/9780743273565-L.jpg"
    "pride-prejudice.jpg" = "https://covers.openlibrary.org/b/isbn/9780141439518-L.jpg"
    "catcher-rye.jpg" = "https://covers.openlibrary.org/b/isbn/9780316769174-L.jpg"
    "dune.jpg" = "https://covers.openlibrary.org/b/isbn/9780441172719-L.jpg"
    "hitchhikers-guide.jpg" = "https://covers.openlibrary.org/b/isbn/9780345391803-L.jpg"
    "enders-game.jpg" = "https://covers.openlibrary.org/b/isbn/9780812550702-L.jpg"
    "foundation.jpg" = "https://covers.openlibrary.org/b/isbn/9780553293357-L.jpg"
    "neuromancer.jpg" = "https://covers.openlibrary.org/b/isbn/9780441569595-L.jpg"
    "hobbit.jpg" = "https://covers.openlibrary.org/b/isbn/9780547928227-L.jpg"
    "harry-potter-1.jpg" = "https://covers.openlibrary.org/b/isbn/9780439708180-L.jpg"
    "name-of-wind.jpg" = "https://covers.openlibrary.org/b/isbn/9780756404741-L.jpg"
    "game-of-thrones.jpg" = "https://covers.openlibrary.org/b/isbn/9780553103540-L.jpg"
    "way-of-kings.jpg" = "https://covers.openlibrary.org/b/isbn/9780765326355-L.jpg"
    "girl-dragon-tattoo.jpg" = "https://covers.openlibrary.org/b/isbn/9780307454546-L.jpg"
    "gone-girl.jpg" = "https://covers.openlibrary.org/b/isbn/9780307588371-L.jpg"
    "da-vinci-code.jpg" = "https://covers.openlibrary.org/b/isbn/9780307474278-L.jpg"
    "big-little-lies.jpg" = "https://covers.openlibrary.org/b/isbn/9780399167065-L.jpg"
    "notebook.jpg" = "https://covers.openlibrary.org/b/isbn/9781455582877-L.jpg"
    "me-before-you.jpg" = "https://covers.openlibrary.org/b/isbn/9780143124542-L.jpg"
    "fault-in-stars.jpg" = "https://covers.openlibrary.org/b/isbn/9780142424179-L.jpg"
    "sapiens.jpg" = "https://covers.openlibrary.org/b/isbn/9780062316110-L.jpg"
    "educated.jpg" = "https://covers.openlibrary.org/b/isbn/9780399590504-L.jpg"
    "thinking-fast-slow.jpg" = "https://covers.openlibrary.org/b/isbn/9780374533557-L.jpg"
    "steve-jobs.jpg" = "https://covers.openlibrary.org/b/isbn/9781451648539-L.jpg"
    "becoming.jpg" = "https://covers.openlibrary.org/b/isbn/9781524763138-L.jpg"
    "anne-frank.jpg" = "https://covers.openlibrary.org/b/isbn/9780553296983-L.jpg"
    "atomic-habits.jpg" = "https://covers.openlibrary.org/b/isbn/9780735211292-L.jpg"
    "7-habits.jpg" = "https://covers.openlibrary.org/b/isbn/9781982137274-L.jpg"
    "win-friends.jpg" = "https://covers.openlibrary.org/b/isbn/9780671027032-L.jpg"
    "power-of-now.jpg" = "https://covers.openlibrary.org/b/isbn/9781577314806-L.jpg"
    "zero-to-one.jpg" = "https://covers.openlibrary.org/b/isbn/9780804139298-L.jpg"
    "lean-startup.jpg" = "https://covers.openlibrary.org/b/isbn/9780307887894-L.jpg"
    "good-to-great.jpg" = "https://covers.openlibrary.org/b/isbn/9780066620992-L.jpg"
    "short-history.jpg" = "https://covers.openlibrary.org/b/isbn/9780767908184-L.jpg"
    "guns-of-august.jpg" = "https://covers.openlibrary.org/b/isbn/9780345476098-L.jpg"
    "brief-history-time.jpg" = "https://covers.openlibrary.org/b/isbn/9780553380163-L.jpg"
    "selfish-gene.jpg" = "https://covers.openlibrary.org/b/isbn/9780198788607-L.jpg"
    "clean-code.jpg" = "https://covers.openlibrary.org/b/isbn/9780132350884-L.jpg"
    "pragmatic-programmer.jpg" = "https://covers.openlibrary.org/b/isbn/9780135957059-L.jpg"
    "algorithms-live-by.jpg" = "https://covers.openlibrary.org/b/isbn/9781627790369-L.jpg"
}

$successCount = 0
$failCount = 0

foreach ($book in $books.GetEnumerator()) {
    $filename = $book.Key
    $url = $book.Value
    $outputPath = Join-Path $booksDir $filename
    
    try {
        Write-Host "Downloading $filename..." -NoNewline
        Invoke-WebRequest -Uri $url -OutFile $outputPath -ErrorAction Stop
        Write-Host " ✓" -ForegroundColor Green
        $successCount++
        Start-Sleep -Milliseconds 500  # Be nice to the API
    }
    catch {
        Write-Host " ✗ (Failed)" -ForegroundColor Red
        $failCount++
    }
}

Write-Host "`n📊 Summary:" -ForegroundColor Cyan
Write-Host "   ✓ Downloaded: $successCount" -ForegroundColor Green
Write-Host "   ✗ Failed: $failCount" -ForegroundColor Red

if ($successCount -gt 0) {
    Write-Host "`n✨ Book covers downloaded successfully!" -ForegroundColor Green
    Write-Host "   Location: $booksDir" -ForegroundColor Yellow
    Write-Host "`n🚀 Next steps:" -ForegroundColor Cyan
    Write-Host "   1. Run: mvn spring-boot:run" -ForegroundColor White
    Write-Host "   2. Visit: http://localhost:8080/" -ForegroundColor White
}
else {
    Write-Host "`n⚠️  No covers downloaded. Check your internet connection." -ForegroundColor Yellow
}
