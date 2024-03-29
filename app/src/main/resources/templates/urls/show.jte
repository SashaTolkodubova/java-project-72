@import hexlet.code.dto.urls.UrlPage
@import hexlet.code.util.NamedRoutes
@param UrlPage page

@template.layout.page(
page = page,
content = @`
<section>
    @if(page != null && page.getFlash() != null)
        <div class="rounded-0 m-0 alert alert-dismissible fade show alert-${page.getFlashType()}" role="alert">
            <p class="m-0">${page.getFlash()}</p>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="close"></button>
        </div>
    @endif
<div class="container-lg mt-5">
    <h1>Сайт: ${page.getUrl().getName()}</h1>

</div>
</section>
`)
